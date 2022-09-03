package sg.edu.nus.iss.team1.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.team1.domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query("SELECT m FROM Message m WHERE m.user.Id=?1 and m.group.Id=?2 ")
    List<Message> findMessageByGroupAndUser(Integer userId, Integer groupId);

    @Query("SELECT c from Message c WHERE c.group.Id = :id")
    List<Message> findMessageByGroupId(@Param("id") Integer id);

    @Query("SELECT m FROM Message m WHERE m.user.Id=:userId and m.individualChat=true ")
    List<Message> findindividualMessageBySender(Integer userId);

    @Query("SELECT m FROM Message m WHERE m.user.Id=:senderId and m.receiverId=:receiverId and m.individualChat=true ")
    List<Message> findindividualMessageByReceiver(Integer senderId, Integer receiverId);

    List<Message> findByGroupOrderByTimeDesc(Integer groupId);

    @Query("SELECT m FROM Message m WHERE m.user.Id=:userId and m.individualChat=true group by m.receiverId")
    List<Message> getChatHistoryBySenderId(Integer userId);

    @Query("SELECT m FROM Message m WHERE m.receiverId=:userId and m.individualChat=true group by m.user.Id")
    List<Message> getChatHistoryByReceiverId(Integer userId);

    @Query("SELECT c from Message c WHERE c.group.Id = :id")
    Page<Message> findMessageByGroupIdwithPagination(@Param("id") Integer id, Pageable pageable);

    @Query(value = "SELECT Count(m) from Message m WHERE  m.user.Id = :id AND m.time BETWEEN :startDate AND :endDate")
    Integer findAllByUserAndTimeBetween(@Param("id") Integer UserId, @Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT Count(m) from Message m WHERE  m.user.Id = :id AND m.group.Id = :groupId AND m.time BETWEEN :startDate AND :endDate")
    Integer findAllByUserAndGroupAndTimeBetween(@Param("id") Integer UserId,
                                                @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate,
                                                @Param("groupId") Integer groupId);

    @Query(value = "SELECT m from Message m WHERE  m.user.Id = :id AND m.time BETWEEN :startDate AND :endDate")
    List<Message> getusermessagesperday(@Param("id") Integer UserId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
