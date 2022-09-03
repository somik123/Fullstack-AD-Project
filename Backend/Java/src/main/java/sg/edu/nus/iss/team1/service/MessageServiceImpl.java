package sg.edu.nus.iss.team1.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.team1.domain.Message;
import sg.edu.nus.iss.team1.domain.StudyGroup;
import sg.edu.nus.iss.team1.domain.User;
import sg.edu.nus.iss.team1.dto.DashboardMessageDTO;
import sg.edu.nus.iss.team1.dto.MessageDTO;
import sg.edu.nus.iss.team1.dto.StudyGroupDTO;
import sg.edu.nus.iss.team1.dto.UserDTO;
import sg.edu.nus.iss.team1.repository.MessageRepository;
import sg.edu.nus.iss.team1.util.MLApi;
import sg.edu.nus.iss.team1.util.OffsetBasedPageRequest;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserService userService;

    @Autowired
    StudyGroupService groupService;

    @Override
    public Boolean savegroupMessage(MessageDTO messageDTO) {
        LocalDateTime localDateTime = LocalDateTime.now();
        User existingUser = userService.findById(messageDTO.getUserId());
        StudyGroup existingGroup = groupService.findById(messageDTO.getGroupId());
        if (existingGroup != null && existingUser != null) {
            Message messageNew = new Message();
            messageNew.setText(messageDTO.getMessage());
            messageNew.setUser(existingUser);
            messageNew.setTime(localDateTime);
            messageNew.setReplyTold(messageDTO.getReplyTold());
            messageNew.setGroup(existingGroup);
            messageNew.setColor(analyseMessage(messageDTO.getMessage()));
            messageNew.setIndividualChat(false);
            messageRepository.save(messageNew);
            // update the group active time for sorting chat history
            existingGroup.setLastactiveTime(localDateTime);
            groupService.saveStudyGroup(existingGroup);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateMessage(MessageDTO messageDTO) {
        Message existingMessage = messageRepository.findById(messageDTO.getId()).get();
        if (existingMessage != null) {
            existingMessage.setText(messageDTO.getMessage());
            existingMessage.setColor(analyseMessage(messageDTO.getMessage()));
            messageRepository.save(existingMessage);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void deleteById(Integer id) {
        messageRepository.deleteById(id);
    }

    @Override
    public Optional<Message> findById(Integer Id) {
        Optional<Message> message = messageRepository.findById(Id);
        return message;
    }

    @Override
    public List<MessageDTO> findMessageByGroupAndUser(Integer userid, Integer groupid) {
        List<Message> messages = (List<Message>) messageRepository.findMessageByGroupAndUser(userid, groupid);
        List<MessageDTO> messaageDTOList = new ArrayList<MessageDTO>();
        MessageDTO messageDTO = new MessageDTO();
        for (Message message : messages) {
            messageDTO.setId(message.getId());
            messageDTO.setMessage(message.getText());
            messageDTO.setUserId(message.getUser().getId());
            messageDTO.setUserName(message.getUser().getUsername());
            messageDTO.setTime(message.getTime());
            messageDTO.setReplyTold(message.getReplyTold());
            messageDTO.setGroupId(message.getGroup().getId());
            messageDTO.setColor(message.getColor());
            messageDTO.setIndividualChat(false);
            messaageDTOList.add(messageDTO);
        }
        return messaageDTOList;
    }

    @Override
    public List<Message> findMessageByGroupId(Integer groupid) {
        List<Message> messages = (List<Message>) messageRepository.findMessageByGroupId(groupid);
        return messages;
    }

    protected int analyseMessage(String stringMessage) {
        int maxSleep = 10; // seconds

        MLApi mlApi = new MLApi();
        mlApi.analyzeMessage(stringMessage);

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

        try {
            return Integer.parseInt(reply);
        } catch (Exception e) {
            System.out.println(e);
        }

        return 3;
    }

    //old one
    @Override
    public Boolean saveindividualMessage(MessageDTO messageDTO) {
        LocalDateTime localDateTime = LocalDateTime.now();
        User senderUser = userService.findById(messageDTO.getUserId());
        User receiverUser = userService.findById(messageDTO.getReceiverId());
        if (senderUser != null && receiverUser != null) {
            Message messageNew = new Message();
            messageNew.setText(messageDTO.getMessage());
            messageNew.setUser(senderUser);
            messageNew.setTime(localDateTime);
            messageNew.setReplyTold(messageDTO.getReplyTold());
            messageNew.setColor(analyseMessage(messageDTO.getMessage()));
            messageNew.setIndividualChat(true);
            messageNew.setReceiverName(receiverUser.getUsername());
            messageNew.setReceiverId(receiverUser.getId());
            messageRepository.save(messageNew);
            return true;
        }
        return false;
    }

    @Override
    public List<MessageDTO> findindividualMessageBySender(Integer userId) {
        List<Message> messages = (List<Message>) messageRepository.findindividualMessageBySender(userId);
        List<MessageDTO> messaageDTOList = new ArrayList<MessageDTO>();
        MessageDTO messageDTO;
        for (Message message : messages) {
            messageDTO = new MessageDTO();
            messageDTO.setId(message.getId());
            messageDTO.setMessage(message.getText());
            messageDTO.setUserId(message.getUser().getId());
            messageDTO.setUserName(message.getUser().getUsername());
            messageDTO.setTime(message.getTime());
            messageDTO.setReplyTold(message.getReplyTold());
            messageDTO.setColor(message.getColor());
            messageDTO.setIndividualChat(true);
            messageDTO.setReceiverName(message.getReceiverName());
            messageDTO.setReceiverId(message.getReceiverId());
            messaageDTOList.add(messageDTO);
        }
        return messaageDTOList;
    }

    @Override
    public List<MessageDTO> findindividualMessageBysenderIdandreceiverId(Integer senderId, Integer receiverId) {
        List<Message> messages = (List<Message>) messageRepository.findindividualMessageByReceiver(senderId,
                receiverId);
        List<Message> reverseMessages = (List<Message>) messageRepository.findindividualMessageByReceiver(receiverId,
                senderId);
        messages.addAll(reverseMessages);
        Collections.sort(messages, (o1, o2) -> o1.getTime().compareTo(o2.getTime()));

        List<MessageDTO> messaageDTOList = new ArrayList<MessageDTO>();
        MessageDTO messageDTO;
        for (Message message : messages) {
            messageDTO = new MessageDTO();
            messageDTO.setId(message.getId());
            messageDTO.setMessage(message.getText());
            messageDTO.setUserId(message.getUser().getId());
            messageDTO.setUserName(message.getUser().getUsername());
            messageDTO.setTime(message.getTime());
            messageDTO.setReplyTold(message.getReplyTold());
            messageDTO.setColor(message.getColor());
            messageDTO.setIndividualChat(true);
            messageDTO.setReceiverName(message.getReceiverName());
            messageDTO.setReceiverId(message.getReceiverId());
            messaageDTOList.add(messageDTO);
        }
        return messaageDTOList;
    }

    @Override
    public List<UserDTO> findchathistoryusersBysenderId(Integer userId) {
        List<Message> messages = messageRepository.getChatHistoryBySenderId(userId);

        List<UserDTO> chatuserList = new ArrayList<UserDTO>();
        Set<Integer> userIdSet = new HashSet<>();
        if (messages != null && messages.size() > 0) {
            for (Message message : messages) {
                userIdSet.add(message.getReceiverId());
            }
        }

        List<Message> receiverMessages = messageRepository.getChatHistoryByReceiverId(userId);
        if (receiverMessages != null && receiverMessages.size() > 0) {
            for (Message message : receiverMessages) {
                userIdSet.add(message.getUser().getId());
            }
        }

        for (Integer userID : userIdSet) {
            User existingUser = userService.findById(userID);
            UserDTO chatUser = new UserDTO();
            chatUser.setId(existingUser.getId());
            chatUser.setUsername(existingUser.getUsername());
            chatUser.setPhoto(existingUser.getPhoto());
            chatUser.setEmail(existingUser.getEmail());
            chatuserList.add(chatUser);
        }

        return chatuserList;
    }

    @Override
    public Boolean createindividualMessage(MessageDTO messageDTO, User sender, User receiver) {
        LocalDateTime localDateTime = LocalDateTime.now();
        List<User> participants = new ArrayList<>();
        participants.add(sender);
        participants.add(receiver);
        StudyGroup individualGroup = groupService.searchindividualGroup(sender, receiver);
        Message messageNew = new Message();
        messageNew.setText(messageDTO.getMessage());
        messageNew.setUser(sender);
        messageNew.setTime(localDateTime);
        messageNew.setReplyTold(messageDTO.getReplyTold());
        messageNew.setColor(analyseMessage(messageDTO.getMessage()));
        messageNew.setGroup(individualGroup);
        messageRepository.save(messageNew);
        // update the group active time for sorting chat history
        individualGroup.setLastactiveTime(localDateTime);
        groupService.saveStudyGroup(individualGroup);
        return true;
    }

    @Override
    public List<Message> getallmessagesByGroupIdwithPagination(Integer groupid, long offset, int limit) {
        Pageable pageable = new OffsetBasedPageRequest(offset, limit, Sort.by("time").descending());
        Page<Message> pageMessage = messageRepository.findMessageByGroupIdwithPagination(groupid, pageable);
        return pageMessage.getContent();
    }

    @Override
    public Integer getallmessagesByUserIdperweek(Integer userId, Integer totalday) {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTimepast7days = LocalDateTime.now().minusDays(totalday);
        return messageRepository.findAllByUserAndTimeBetween(userId, localDateTimepast7days, localDateTime);
    }

    @Override
    public Integer getmessagesByUserandGroupperWeek(Integer userId, Integer totalday, Integer groupId) {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTimepast7days = LocalDateTime.now().minusDays(totalday);
        return messageRepository.findAllByUserAndGroupAndTimeBetween(userId, localDateTimepast7days, localDateTime,
                groupId);
    }

    @Override
    public List<DashboardMessageDTO> getusermessagesperday(Integer userId, Integer totalday) {
        List<DashboardMessageDTO> dashboardmessageDTO = new ArrayList<>();
        List<Integer> onedaymessagesList = new ArrayList<>();
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTimepast7days = LocalDateTime.now().minusDays(totalday);
        List<Message> messages = messageRepository.getusermessagesperday(userId, localDateTimepast7days, localDateTime);
        DashboardMessageDTO messageDTO = new DashboardMessageDTO();
        Set<StudyGroup> groupSet = new HashSet<StudyGroup>();
        Set<StudyGroupDTO> groupDTOSet = new HashSet<StudyGroupDTO>();
        for (Message message : messages) {
            messageDTO.setTodayDate(localDateTime);
            messageDTO.setLastDate(localDateTimepast7days);
            messageDTO.setUserId(message.getUser().getId());
            messageDTO.setUserName(message.getUser().getUsername());
            groupSet.add(message.getGroup());
        }
        for (StudyGroup studyGroup : groupSet) {
            StudyGroupDTO groupDTO = new StudyGroupDTO();
            groupDTO.setName(studyGroup.getName());
            groupDTO.setId(studyGroup.getId());
            groupDTO.setIsPrivate(studyGroup.getIsPrivate());
            groupDTOSet.add(groupDTO);
        }
        for (int i = 1; i <= totalday; i++) {
            int onedayMessages = getallmessagesByUserIdperweek(userId, i);
            onedaymessagesList.add(onedayMessages);
        }
        messageDTO.setTotalMessages(onedaymessagesList);
        messageDTO.setGroup(groupDTOSet);
        messageDTO.setTotalGroups(groupDTOSet.size());
        dashboardmessageDTO.add(messageDTO);

        return dashboardmessageDTO;
    }

    public List<LocalDate> toDateList(String startDate, String endDate) {
        final LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        final LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        final long days = start.until(end, ChronoUnit.DAYS);

        return LongStream.rangeClosed(0, days).mapToObj(start::plusDays).collect(Collectors.toList());
    }
}
