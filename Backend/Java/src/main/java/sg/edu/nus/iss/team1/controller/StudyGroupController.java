package sg.edu.nus.iss.team1.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.team1.domain.StudyGroup;
import sg.edu.nus.iss.team1.domain.User;
import sg.edu.nus.iss.team1.dto.StudyGroupDTO;
import sg.edu.nus.iss.team1.service.StudyGroupService;
import sg.edu.nus.iss.team1.service.UserService;

@RestController
@RequestMapping(path = "/group", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class StudyGroupController {
    @Autowired
    StudyGroupService studyGroupService;

    @Autowired
    UserService userService;

    // create studyGroup
    @PostMapping("/")
    ResponseEntity<?> createStudyGroup(@RequestBody StudyGroupDTO studyGroup) throws Exception {
        StudyGroup existingStudyGroup = studyGroupService.findByName(studyGroup.getName());
        if (existingStudyGroup != null) {
            return new ResponseEntity<>("The same studyGroup already Existed!", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(studyGroupService.saveStudyGroupDTO(studyGroup));
    }

    // get by id
    @GetMapping("/{id}")
    public ResponseEntity<?> findStudyGroupById(@PathVariable Integer id) {
        StudyGroup studyGroup = studyGroupService.findById(id);
        if (studyGroup != null) {
            StudyGroupDTO studygroupDTO = new StudyGroupDTO();
            studygroupDTO.setId(studyGroup.getId());
            studygroupDTO.setName(studyGroup.getName());
            studygroupDTO.setIsPrivate(studyGroup.getIsPrivate());
            studygroupDTO.setCreateDate(studyGroup.getCreateDate());
            studygroupDTO.setCreatorId(studyGroup.getCreator().getId());
            studygroupDTO.setCreatorName(studyGroup.getCreator().getUsername());

            List<Integer> participantIds = new ArrayList<>();
            for (User u : studyGroup.getParticipants()) {
                Integer uid = u.getId();
                participantIds.add(uid);

            }

            studygroupDTO.setParticipantsId(participantIds);
            return new ResponseEntity<>(studygroupDTO, HttpStatus.OK);

        } else {
            return new ResponseEntity<>("No studyGroup with this Id " + id + " exist.", HttpStatus.NOT_FOUND);
        }

    }

    // get by user Id
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> findStudyGroupsByUserId(@PathVariable Integer userId) {

        List<StudyGroupDTO> studyGroupList = studyGroupService.findStudyGroupsByUserId(userId);

        if (studyGroupList != null) {
            return new ResponseEntity<>(studyGroupList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No studyGroup with this creator id " + userId + " exist.",
                    HttpStatus.NOT_FOUND);
        }

    }

    // update studyGroup
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudyGroup(@RequestBody StudyGroup newStudyGroup, @PathVariable Integer id) {
        Optional<StudyGroup> userOptional = Optional.of(studyGroupService.findById(id));
        if (userOptional.isPresent()) {
            StudyGroup studyGroup = userOptional.get();
            studyGroup.setName(newStudyGroup.getName());
            studyGroup.setCreateDate(newStudyGroup.getCreateDate());
            return new ResponseEntity<>("user updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No book with this Id " + id + " exist.", HttpStatus.NOT_FOUND);
        }

    }

    // delete by id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        studyGroupService.deleteById(id);
        return new ResponseEntity<>("StudyGroup deleted", HttpStatus.OK);
    }

    @PostMapping("/join/{gid}/user/{uid}")
    public ResponseEntity<?> addStudyGroupUser(@PathVariable Integer gid, @PathVariable Integer uid) {
        StudyGroup studyGroup = studyGroupService.findById(gid);
        StudyGroupDTO studyGroupDTO = new StudyGroupDTO();
        if (studyGroup == null) {
            return new ResponseEntity<>("Study Group is not exist", HttpStatus.NOT_FOUND);
        } else {
            User user = userService.findById(uid);
            if (userService.findById(uid) == null) {
                return new ResponseEntity<>("No user exist with Id " + uid, HttpStatus.NOT_FOUND);
            } else {
                studyGroupDTO = studyGroupService.joinGroup(user, studyGroup);
            }
            return new ResponseEntity<>(studyGroupDTO, HttpStatus.OK);
        }
    }

    // private group == one to one chat
    // check existing private group exist for two users
    @GetMapping("/check/{senderId}/{receiverId}")
    public ResponseEntity<?> checkindividualgroupExist(@PathVariable Integer senderId,
                                                       @PathVariable Integer receiverId) {
        List<User> participantUsers = new ArrayList<>();

        User senderUser = userService.findById(senderId);
        User receiverUser = userService.findById(receiverId);
        if (senderUser != null && receiverUser != null) {
            participantUsers.add(senderUser);
            participantUsers.add(receiverUser);
            StudyGroup existingGroup = studyGroupService.checkindividualgroupExist(senderUser, receiverUser);
            if (existingGroup != null) {
                return new ResponseEntity<>("Individual group exist", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Individual group does not exist", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("One of the user not exist", HttpStatus.NOT_FOUND);
        }

    }

    // use this to retrieve or create individual group with two specific users
    // create new or retrieve existing individual group with two users
    @PostMapping("/individual/{senderId}/{receiverId}")
    ResponseEntity<?> createindividualGroup(@PathVariable Integer senderId, @PathVariable Integer receiverId) {
        if (senderId.equals(receiverId)) {
            return new ResponseEntity<>("Cannot create same users as participants", HttpStatus.OK);
        } else {
            User senderUser = userService.findById(senderId);
            User receiverUser = userService.findById(receiverId);
            StudyGroup existingStudyGroup = studyGroupService.searchindividualGroup(senderUser, receiverUser);
            StudyGroupDTO responsegroupDTO = new StudyGroupDTO();
            List<Integer> participantidList = new ArrayList<>();
            for (User user : existingStudyGroup.getParticipants()) {
                participantidList.add(user.getId());
            }
            responsegroupDTO.setId(existingStudyGroup.getId());
            responsegroupDTO.setName(existingStudyGroup.getName());
            responsegroupDTO.setIsPrivate(existingStudyGroup.getIsPrivate());
            responsegroupDTO.setCreateDate(existingStudyGroup.getCreateDate());
            responsegroupDTO.setCreatorId(existingStudyGroup.getCreator().getId());
            responsegroupDTO.setCreatorName(existingStudyGroup.getCreator().getName());
            responsegroupDTO.setCreatorPhoto(existingStudyGroup.getCreator().getPhoto());
            responsegroupDTO.setLastActiveTime(existingStudyGroup.getLastactiveTime());
            responsegroupDTO.setParticipantsId(participantidList);
            return new ResponseEntity<>(responsegroupDTO, HttpStatus.OK);
        }
    }

    // get by user Id as participant
    @GetMapping("/individual/user/{userId}")
    public ResponseEntity<?> findIndividualgroupByUserId(@PathVariable Integer userId) {
        List<StudyGroupDTO> studyGroupList = new ArrayList<>();
        User existingUser = userService.findById(userId);
        if (existingUser != null) {
            studyGroupList = studyGroupService.getallindividualgroupasParticipant(existingUser);

            if (studyGroupList != null) {
                return new ResponseEntity<>(studyGroupList, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No individual Group with this participant id " + userId + " exist.",
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);
        }
    }
}
