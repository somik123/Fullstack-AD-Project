package sg.edu.nus.iss.team1.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

public class EventResponse implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;
    private Integer id;
    private String name;
    private String description;
    private String userName;
    private Collection<String> participantName;
    private Collection<String> participantPhoto;
    private LocalDateTime createTime;
    private String groupName;
    private Integer groupId;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    private LocalDateTime eventTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Collection<String> getParticipantName() {
        return participantName;
    }

    public void setParticipantName(Collection<String> participantName) {
        this.participantName = participantName;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Collection<String> getParticipantPhoto() {
        return participantPhoto;
    }

    public void setParticipantPhoto(Collection<String> participantPhoto) {
        this.participantPhoto = participantPhoto;
    }

    public EventResponse() {
        super();
    }

}
