package sg.edu.nus.iss.team1.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.team1.domain.Message;
import sg.edu.nus.iss.team1.domain.StudyGroup;
import sg.edu.nus.iss.team1.domain.User;
import sg.edu.nus.iss.team1.dto.MessageDTO;
import sg.edu.nus.iss.team1.dto.UserDTO;
import sg.edu.nus.iss.team1.service.MessageService;
import sg.edu.nus.iss.team1.service.StudyGroupService;
import sg.edu.nus.iss.team1.service.UserService;

@RestController
@RequestMapping(path = "/message", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    StudyGroupService studyGroupService;

    @PostMapping("/create")
    ResponseEntity<?> SendMessage(@RequestBody MessageDTO newMessage) {
        try {
            Boolean status = messageService.savegroupMessage(newMessage);
            if (status) {
                return new ResponseEntity<>("Message Saved", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Cannot create message for user id: " + newMessage.getUserId(),
                        HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<?> deleteMessage(@PathVariable Integer id) {
        messageService.deleteById(id);
        return new ResponseEntity<>("Message Deleted", HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    ResponseEntity<?> replaceMessage(@RequestBody MessageDTO newMessage, @PathVariable Integer id) {
        boolean check = messageService.updateMessage(newMessage);
        if (check) {
            return new ResponseEntity<>("Message Updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No message found to update with id: " + newMessage.getId(), HttpStatus.OK);
        }
    }

    @GetMapping("/user/{user_id}/group/{group_id}")
    public ResponseEntity<?> findMessageByGroupAndUser(@PathVariable Integer user_id, @PathVariable Integer group_id) {
        User existingUser = userService.findById(user_id);
        List<MessageDTO> messaageDTOList = null;
        if (existingUser != null) {
            StudyGroup existingGroup = studyGroupService.findById(group_id);
            if (existingGroup != null) {
                messaageDTOList = messageService.findMessageByGroupAndUser(user_id, group_id);
            } else {
                return new ResponseEntity<>("No group found for Id: " + group_id, HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("No user found for Id: " + user_id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(messaageDTOList, HttpStatus.OK);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<?> findMessagesByGroupId(@PathVariable Integer groupId) {

        List<Message> messageList = messageService.findMessageByGroupId(groupId);
        if (messageList != null) {

            List<MessageDTO> messaageDTOList = new ArrayList<MessageDTO>();
            for (Message message : messageList) {
                MessageDTO messageDTO = new MessageDTO();
                messageDTO.setId(message.getId());
                messageDTO.setMessage(message.getText());
                messageDTO.setUserId(message.getUser().getId());
                messageDTO.setUserName(message.getUser().getUsername());
                messageDTO.setTime(message.getTime());
                messageDTO.setReplyTold(message.getReplyTold());
                messageDTO.setGroupId(message.getGroup().getId());
                messageDTO.setUserPhoto(message.getUser().getPhoto());
                messageDTO.setColor(message.getColor());
                messaageDTOList.add(messageDTO);
            }

            return new ResponseEntity<>(messaageDTOList, HttpStatus.OK);
        }
        return new ResponseEntity<>("No group with group id " + groupId + " exist", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/individual/create")
    ResponseEntity<?> SenddonetooneMessage(@RequestBody MessageDTO newMessage) {
        try {
            Boolean status = messageService.saveindividualMessage(newMessage);
            if (status) {
                return new ResponseEntity<>("Message Saved", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Cannot create message for user id: " + newMessage.getUserId(),
                        HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/individual/{userId}")
    public ResponseEntity<?> findindividualMessagesByUserId(@PathVariable Integer userId) {
        List<MessageDTO> messageList = messageService.findindividualMessageBySender(userId);
        if (messageList.size() > 0) {
            return new ResponseEntity<>(messageList, HttpStatus.OK);
        }
        return new ResponseEntity<>("Cannot retrieve individual message for this user" + userId, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/individual/{senderId}/receiver/{receiverId}")
    public ResponseEntity<?> findindividualMessagesBysenderIdandreceiverId(@PathVariable Integer senderId,
                                                                           @PathVariable Integer receiverId) {
        User senderUser = userService.findById(senderId);
        User receiverUser = userService.findById(receiverId);

        if (senderUser != null && receiverUser != null) {
            List<MessageDTO> messageList = messageService.findindividualMessageBysenderIdandreceiverId(senderId,
                    receiverId);
            if (messageList != null) {
                return new ResponseEntity<>(messageList, HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    "No messages between sender Id: " + senderId + " and receiver Id: " + receiverId,
                    HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>("One of the user not exist", HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/individual/chathistory/{userId}")
    public ResponseEntity<?> findchathistoryusersBysenderId(@PathVariable Integer userId) {
        User senderUser = userService.findById(userId);
        if (senderUser != null) {
            List<UserDTO> userList = messageService.findchathistoryusersBysenderId(userId);
            if (userList != null) {
                return new ResponseEntity<>(userList, HttpStatus.OK);
            }
            return new ResponseEntity<>("No chat history for this user Id: " + userId, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/individualchat/send/{senderId}/{receiverId}")
    ResponseEntity<?> sendindividualMessage(@PathVariable Integer senderId, @PathVariable Integer receiverId,
                                            @RequestBody MessageDTO newMessage) {
        User senderUser = userService.findById(senderId);
        User receiverUser = userService.findById(receiverId);
        if (senderUser != null && receiverUser != null) {
            Boolean status = messageService.createindividualMessage(newMessage, senderUser, receiverUser);
            if (status) {
                return new ResponseEntity<>("Message Saved", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Cannot send message", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("One of the user does not exist", HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/individualchat/retrieve/{senderId}/{receiverId}")
    public ResponseEntity<?> retrieveindividualMessagesBysenderIdandreceiverId(@PathVariable Integer senderId,
                                                                               @PathVariable Integer receiverId) {
        User senderUser = userService.findById(senderId);
        User receiverUser = userService.findById(receiverId);
        List<MessageDTO> messaageDTOList = new ArrayList<MessageDTO>();
        if (senderUser != null && receiverUser != null) {
            StudyGroup existingGroup = studyGroupService.searchindividualGroup(senderUser, receiverUser);
            if (existingGroup != null) {
                List<Message> messageList = messageService.findMessageByGroupId(existingGroup.getId());
                if (messageList != null) {
                    for (Message message : messageList) {
                        MessageDTO messageDTO = new MessageDTO();
                        messageDTO.setId(message.getId());
                        messageDTO.setMessage(message.getText());
                        messageDTO.setUserId(message.getUser().getId());
                        messageDTO.setUserName(message.getUser().getUsername());
                        messageDTO.setTime(message.getTime());
                        messageDTO.setReplyTold(message.getReplyTold());
                        messageDTO.setGroupId(message.getGroup().getId());
                        messageDTO.setUserPhoto(message.getUser().getPhoto());
                        messageDTO.setColor(message.getColor());
                        messaageDTOList.add(messageDTO);
                    }
                }
            } else {
                return new ResponseEntity<>("No existing individual group found with two users", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("One of the user not exist", HttpStatus.NOT_FOUND);
        }
        if (messaageDTOList.size() > 0) {
            return new ResponseEntity<>(messaageDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No messages", HttpStatus.OK);
        }

    }

    // find message by Group Id with pagination
    @GetMapping("/pagination/group/{groupId}") // ?limit=25&offset=50
    public ResponseEntity<?> findMessagesByGroupIdwithPagination(@PathVariable Integer groupId,
                                                                 @RequestParam(defaultValue = "0") Long offset, @RequestParam(defaultValue = "20") Integer limit) {

        List<Message> messageList = messageService.getallmessagesByGroupIdwithPagination(groupId, offset, limit);
        if (messageList != null) {

            List<MessageDTO> messaageDTOList = new ArrayList<MessageDTO>();
            for (Message message : messageList) {
                MessageDTO messageDTO = new MessageDTO();
                messageDTO.setId(message.getId());
                messageDTO.setMessage(message.getText());
                messageDTO.setUserId(message.getUser().getId());
                messageDTO.setUserName(message.getUser().getUsername());
                messageDTO.setTime(message.getTime());
                messageDTO.setReplyTold(message.getReplyTold());
                messageDTO.setGroupId(message.getGroup().getId());
                messageDTO.setUserPhoto(message.getUser().getPhoto());
                messageDTO.setColor(message.getColor());
                messaageDTOList.add(messageDTO);
            }

            return new ResponseEntity<>(messaageDTOList, HttpStatus.OK);
        }
        return new ResponseEntity<>("No group with group id " + groupId + " exist", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{userid}/days/{totalday}")
    public ResponseEntity<?> getmessagesByUserperWeek(@PathVariable Integer userid, @PathVariable Integer totalday) {
        User existingUser = userService.findById(userid);
        if (existingUser != null) {
            return new ResponseEntity<>(messageService.getallmessagesByUserIdperweek(userid, totalday), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No user with user id " + userid + " exist", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{userid}/groups/{groupId}/days/{totalday}")
    public ResponseEntity<?> getmessagesByUserandGroupperWeek(@PathVariable Integer userid,
                                                              @PathVariable Integer totalday, @PathVariable Integer groupId) {
        User existingUser = userService.findById(userid);
        if (existingUser != null) {
            StudyGroup existingGroup = studyGroupService.findById(groupId);
            if (existingGroup != null) {
                return new ResponseEntity<>(messageService.getmessagesByUserandGroupperWeek(userid, totalday, groupId),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No group with group id " + groupId + " exist", HttpStatus.NOT_FOUND);
            }

        } else {
            return new ResponseEntity<>("No user with user id " + userid + " exist", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/detail/{userid}/days/{totalday}")
    public ResponseEntity<?> getusermessagesperday(@PathVariable Integer userid, @PathVariable Integer totalday) {
        User existingUser = userService.findById(userid);
        if (existingUser != null) {
            return new ResponseEntity<>(messageService.getusermessagesperday(userid, totalday), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No user with user id " + userid + " exist", HttpStatus.NOT_FOUND);
        }
    }

}
