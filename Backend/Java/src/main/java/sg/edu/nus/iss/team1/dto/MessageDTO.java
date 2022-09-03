package sg.edu.nus.iss.team1.dto;

import java.time.LocalDateTime;

public class MessageDTO {
    private Integer Id;
    private String message;
    private Integer userId;
    private String userName;
    private LocalDateTime time;
    private Integer replyTold;
    private Integer groupId;
    private String userPhoto;
    private Integer color;
    private boolean individualChat;
    private String receiverName;
    private Integer receiverId;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Integer getReplyTold() {
        return replyTold;
    }

    public void setReplyTold(Integer replyTold) {
        this.replyTold = replyTold;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public boolean isIndividualChat() {
        return individualChat;
    }

    public void setIndividualChat(boolean individualChat) {
        this.individualChat = individualChat;
    }


    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public MessageDTO(Integer id, String message, Integer userId, String userName, LocalDateTime time,
                      Integer replyTold, Integer groupId, String userPhoto, Integer color, boolean individualChat,
                      String receiverName, Integer receiverId) {
        super();
        Id = id;
        this.message = message;
        this.userId = userId;
        this.userName = userName;
        this.time = time;
        this.replyTold = replyTold;
        this.groupId = groupId;
        this.userPhoto = userPhoto;
        this.color = color;
        this.individualChat = individualChat;
        this.receiverName = receiverName;
        this.receiverId = receiverId;
    }

    public MessageDTO() {
        super();
        // TODO Auto-generated constructor stub
    }

}
