package sg.edu.nus.iss.team1.service;

import java.util.List;
import java.util.Optional;

import sg.edu.nus.iss.team1.domain.Message;
import sg.edu.nus.iss.team1.domain.User;
import sg.edu.nus.iss.team1.dto.DashboardMessageDTO;
import sg.edu.nus.iss.team1.dto.MessageDTO;
import sg.edu.nus.iss.team1.dto.UserDTO;

public interface MessageService {
    public Boolean savegroupMessage(MessageDTO message);

    public void deleteById(Integer id);

    public Optional<Message> findById(Integer Id);

    public List<MessageDTO> findMessageByGroupAndUser(Integer userid, Integer groupid);

    List<Message> findMessageByGroupId(Integer groupid);

    boolean updateMessage(MessageDTO messageDTO);

    Boolean saveindividualMessage(MessageDTO messageDTO);

    List<MessageDTO> findindividualMessageBySender(Integer userId);

    List<MessageDTO> findindividualMessageBysenderIdandreceiverId(Integer senderId, Integer receiverId);

    List<UserDTO> findchathistoryusersBysenderId(Integer userId);

    Boolean createindividualMessage(MessageDTO messageDTO, User sender, User receiver);

    List<Message> getallmessagesByGroupIdwithPagination(Integer groupid, long offset, int limit);

    Integer getallmessagesByUserIdperweek(Integer userId, Integer totalday);

    Integer getmessagesByUserandGroupperWeek(Integer userId, Integer totalday, Integer groupId);

    List<DashboardMessageDTO> getusermessagesperday(Integer userId, Integer totalday);

}
