package edu.iss.nus.group1.studybuddy.dto;

import java.util.List;

public class GroupDTO {
    private Integer id;
    private String name;
    private Boolean isPrivate;
    private String createDate;
    private Integer creatorId;
    private String creatorName;
    private List<Integer> participantsId;
    private String description;
    private String lastActiveTime;


    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public List<Integer> getParticipantsId() {
        return participantsId;
    }

    public String getDescription() {
        return description;
    }

    public String getLastActiveTime() {
        return lastActiveTime;
    }

    @Override
    public String toString() {
        return "GroupDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isPrivate=" + isPrivate +
                ", createDate='" + createDate + '\'' +
                ", creatorId=" + creatorId +
                ", creatorName='" + creatorName + '\'' +
                ", participantsId=" + participantsId +
                ", description='" + description + '\'' +
                ", lastactiveTime='" + lastActiveTime + '\'' +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }
}
