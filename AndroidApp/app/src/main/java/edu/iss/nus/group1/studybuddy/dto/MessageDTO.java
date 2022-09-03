package edu.iss.nus.group1.studybuddy.dto;

public class MessageDTO {
    private Integer id;
    private String message;
    private Integer userId;
    private String userName;
    private String time;
    private Integer replyTold;
    private Integer groupId;
    private String userPhoto;
    private Integer color;

    public Integer getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getTime() {
        return time;
    }

    public Integer getReplyTold() {
        return replyTold;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public Integer getColor() {
        return color;
    }
}
