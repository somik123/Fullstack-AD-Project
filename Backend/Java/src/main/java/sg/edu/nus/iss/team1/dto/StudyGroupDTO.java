package sg.edu.nus.iss.team1.dto;

import java.time.LocalDateTime;
import java.util.List;

public class StudyGroupDTO {
    private Integer Id;
    private String name;
    private Boolean isPrivate;
    private LocalDateTime createDate;
    private Integer creatorId;
    private String creatorName;
    private List<Integer> participantsId;
    private String description;
    private String creatorPhoto;
    private LocalDateTime lastActiveTime;

    public StudyGroupDTO(Integer id, String name, Boolean isPrivate, LocalDateTime createDate, Integer creatorId,
                         String creatorName, List<Integer> participantsId, String description, String creatorPhoto,
                         LocalDateTime lastActiveTime) {
        super();
        Id = id;
        this.name = name;
        this.isPrivate = isPrivate;
        this.createDate = createDate;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.participantsId = participantsId;
        this.description = description;
        this.creatorPhoto = creatorPhoto;
        this.lastActiveTime = lastActiveTime;
    }

    public StudyGroupDTO() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public List<Integer> getParticipantsId() {
        return participantsId;
    }

    public void setParticipantsId(List<Integer> participantsId) {
        this.participantsId = participantsId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatorPhoto() {
        return creatorPhoto;
    }

    public void setCreatorPhoto(String creatorPhoto) {
        this.creatorPhoto = creatorPhoto;
    }

    public LocalDateTime getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(LocalDateTime lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

}
