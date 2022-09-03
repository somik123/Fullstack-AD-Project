package sg.edu.nus.iss.team1.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.team1.domain.Interest;
import sg.edu.nus.iss.team1.domain.StudyGroup;
import sg.edu.nus.iss.team1.domain.User;
import sg.edu.nus.iss.team1.dto.StudyGroupDTO;
import sg.edu.nus.iss.team1.repository.EventRepository;
import sg.edu.nus.iss.team1.repository.StudyGroupRepository;
import sg.edu.nus.iss.team1.repository.UserRepository;
import sg.edu.nus.iss.team1.util.MLApi;

@Service
public class StudyGroupServiceImpl implements StudyGroupService {

    @Autowired
    StudyGroupRepository studyGroupRepository;

    @Autowired
    EventRepository eventRepo;

    @Autowired
    UserRepository userRepo;

    @Override
    public void saveStudyGroup(StudyGroup studyGroup) {
        LocalDateTime localDateTime = LocalDateTime.now();
        studyGroup.setLastactiveTime(localDateTime);
        studyGroupRepository.save(studyGroup);
    }

    @Override
    public StudyGroup findByName(String name) {
        return studyGroupRepository.findByName(name);
    }

    @Override
    public StudyGroup findById(Integer Id) {
        if (studyGroupRepository.findById(Id).isPresent())
            return studyGroupRepository.findById(Id).get();
        return null;
    }

    @Override
    public List<StudyGroup> findByCreateDate(LocalDateTime createDate) {
        return studyGroupRepository.findByCreateDate(createDate);
    }

    @Override
    public void deleteById(Integer id) {
        studyGroupRepository.deleteById(id);
    }

    @Override
    public List<StudyGroup> findAllStudyGroup() {
        return studyGroupRepository.findAll();
    }

    @Override
    public StudyGroupDTO saveStudyGroupDTO(StudyGroupDTO studyGroup) {
        StudyGroupDTO responsegroupDTO = new StudyGroupDTO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localTime = LocalDateTime.now();
        String text = localTime.format(formatter);
        LocalDateTime formatdateTime = LocalDateTime.parse(text, formatter);
        User creatorUser = userRepo.findById(studyGroup.getCreatorId()).get();
        StudyGroup newGroup = null;
        if (creatorUser != null) {
            List<User> participants = new ArrayList<>();
            newGroup = new StudyGroup();
            newGroup.setName(studyGroup.getName());
            newGroup.setIsPrivate(false);
            newGroup.setCreateDate(formatdateTime);
            participants.add(creatorUser);
            newGroup.setParticipants(participants);
            newGroup.setCreator(creatorUser);
            studyGroupRepository.saveAndFlush(newGroup);

            List<Integer> participantidList = new ArrayList<>();
            for (User user : newGroup.getParticipants()) {
                participantidList.add(user.getId());
            }
            responsegroupDTO.setId(newGroup.getId());
            responsegroupDTO.setName(newGroup.getName());
            responsegroupDTO.setIsPrivate(newGroup.getIsPrivate());
            responsegroupDTO.setCreateDate(newGroup.getCreateDate());
            responsegroupDTO.setCreatorId(newGroup.getCreator().getId());
            responsegroupDTO.setCreatorName(newGroup.getCreator().getName());
            responsegroupDTO.setCreatorPhoto(newGroup.getCreator().getPhoto());
            responsegroupDTO.setParticipantsId(participantidList);

        }
        return responsegroupDTO;
    }

    @Override
    public List<StudyGroupDTO> findStudyGroupsByUserId(Integer userId) {
        List<StudyGroupDTO> groupDtoList = new ArrayList<>();
        User existingUser = userRepo.findById(userId).get();
        if (existingUser != null) {
            List<StudyGroup> joinedGroups = studyGroupRepository
                    .findByParticipantsOrderByLastactiveTimeDesc(existingUser);
            for (StudyGroup e : joinedGroups) {
                StudyGroupDTO studygroupDTO = new StudyGroupDTO();
                studygroupDTO.setId(e.getId());
                studygroupDTO.setName(e.getName());
                studygroupDTO.setIsPrivate(e.getIsPrivate());
                studygroupDTO.setCreateDate(e.getCreateDate());
                studygroupDTO.setCreatorId(e.getCreator().getId());
                studygroupDTO.setCreatorName(e.getCreator().getUsername());
                studygroupDTO.setLastActiveTime(e.getLastactiveTime());
                studygroupDTO.setCreatorPhoto(e.getCreator().getPhoto());
                List<Integer> participantIds = new ArrayList<>();
                for (User u : e.getParticipants()) {
                    Integer uid = u.getId();
                    participantIds.add(uid);
                }

                studygroupDTO.setParticipantsId(participantIds);
                groupDtoList.add(studygroupDTO);
            }
        }

        return groupDtoList;

    }

    // To be called to set user's book recommendation order
    protected String bookOrder(int userId) {
        Optional<User> user = userRepo.findById(userId);
        if (user.isPresent()) {
            List<Interest> interest = new ArrayList<>(user.get().getInterest());

            String latestEvent = user.get().get_eventLatest();

            int maxSleep = 10; // seconds

            MLApi mlApi = new MLApi();
            mlApi.recommendBooks(interest, latestEvent);

            System.out.print("Waiting");
            // While thread is still alive
            for (int i = 0; i < maxSleep * 10 && mlApi.getBkThread().isAlive(); i++) {
                try {
                    Thread.sleep(100); // Wait 0.1s at a time for background thread to complete
                    System.out.print(" .");
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
            System.out.println();
            String reply = mlApi.getMlReply();
            System.out.println(reply);

            return reply;
        }
        return "";
    }

    @Override
    public StudyGroupDTO joinGroup(User joinUser, StudyGroup existingGroup) {
        StudyGroupDTO studyGroupDTO = new StudyGroupDTO();

        List<User> participants = (List<User>) existingGroup.getParticipants();
        if (!checkparticipants(participants, joinUser)) {
            participants.add(joinUser);
            existingGroup.setParticipants(participants);
            studyGroupRepository.save(existingGroup);
        }
        List<Integer> participantId = new ArrayList<>();
        for (User user : existingGroup.getParticipants()) {
            participantId.add(user.getId());
        }
        studyGroupDTO.setId(existingGroup.getId());
        studyGroupDTO.setName(existingGroup.getName());
        studyGroupDTO.setIsPrivate(existingGroup.getIsPrivate());
        studyGroupDTO.setCreateDate(existingGroup.getCreateDate());
        studyGroupDTO.setCreatorId(existingGroup.getCreator().getId());
        studyGroupDTO.setCreatorName(existingGroup.getCreator().getUsername());
        studyGroupDTO.setParticipantsId(participantId);
        studyGroupDTO.setCreatorPhoto(existingGroup.getCreator().getPhoto());
        return studyGroupDTO;
    }

    @Override
    public boolean checkparticipants(List<User> participants, User joinUser) {
        for (User user : participants) {
            if (user.getUsername().equalsIgnoreCase(joinUser.getUsername())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public StudyGroup checkindividualgroupExist(User senderUser, User receiverUser) {
        StudyGroup existingGroup = null;
        List<StudyGroup> existingindividualGroup = studyGroupRepository
                .findByParticipantsAndIsPrivateOrderByLastactiveTimeDesc(senderUser, true);
        for (StudyGroup studyGroup : existingindividualGroup) {
            for (User participant : studyGroup.getParticipants()) {
                if (receiverUser.getId() == participant.getId()) {
                    existingGroup = studyGroup;
                    break;
                }
            }
        }
        return existingGroup;
    }

    @Override
    public StudyGroup searchindividualGroup(User senderUser, User receiverUser) {

        List<User> participants = new ArrayList<>();
        participants.add(senderUser);
        participants.add(receiverUser);
        StudyGroup checkGroup = checkindividualgroupExist(senderUser, receiverUser);
        if (checkGroup != null) {
            return checkGroup;
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime localTime = LocalDateTime.now();
            String text = localTime.format(formatter);
            LocalDateTime formatdateTime = LocalDateTime.parse(text, formatter);
            StudyGroup newGroup = null;
            newGroup = new StudyGroup();
            newGroup.setName(senderUser.getUsername() + " | " + receiverUser.getUsername());
            newGroup.setIsPrivate(true);
            newGroup.setCreateDate(formatdateTime);
            newGroup.setParticipants(participants);
            newGroup.setCreator(senderUser);
            studyGroupRepository.saveAndFlush(newGroup);
            return newGroup;
        }

    }

    @Override
    public List<StudyGroupDTO> getallindividualgroupasParticipant(User user) {
        List<StudyGroupDTO> existingGroups = new ArrayList<>();
        List<StudyGroup> existingindividualGroup = studyGroupRepository
                .findByParticipantsAndIsPrivateOrderByLastactiveTimeDesc(user, true);
        if (existingindividualGroup != null) {
            for (StudyGroup e : existingindividualGroup) {
                StudyGroupDTO studygroupDTO = new StudyGroupDTO();
                studygroupDTO.setId(e.getId());
                studygroupDTO.setName(e.getName());
                studygroupDTO.setIsPrivate(e.getIsPrivate());
                studygroupDTO.setCreateDate(e.getCreateDate());
                studygroupDTO.setCreatorId(e.getCreator().getId());
                studygroupDTO.setCreatorName(e.getCreator().getUsername());
                studygroupDTO.setLastActiveTime(e.getLastactiveTime());
                studygroupDTO.setCreatorPhoto(e.getCreator().getPhoto());
                List<Integer> participantIds = new ArrayList<>();
                for (User u : e.getParticipants()) {
                    Integer uid = u.getId();
                    participantIds.add(uid);
                }

                studygroupDTO.setParticipantsId(participantIds);
                existingGroups.add(studygroupDTO);
            }
        }

        return existingGroups;
    }

}
