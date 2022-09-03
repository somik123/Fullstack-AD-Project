package edu.iss.nus.group1.studybuddy.dto;

public class EventDTO {
    private Integer id;
    private String name;
    private String description;
    private String createTime;
    private String eventTime;
    private String userName;
    private String groupName;
    private Integer groupId;
    private String[] participantName;
    private String[] participantPhoto;


    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getUserName() {
        return userName;
    }

    public String getGroupName() {
        return groupName;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public String[] getParticipantName() {
        return participantName;
    }

    public String[] getParticipantPhoto() {
        return participantPhoto;
    }
}
