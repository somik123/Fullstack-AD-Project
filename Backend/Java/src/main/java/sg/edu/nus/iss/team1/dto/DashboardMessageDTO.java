package sg.edu.nus.iss.team1.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class DashboardMessageDTO {
    private List<Integer> totalMessages;
    private Integer totalGroups;
    private Set<StudyGroupDTO> group;
    private LocalDateTime todayDate;
    private LocalDateTime lastDate;
    private String userName;
    private Integer userId;

    public List<Integer> getTotalMessages() {
        return totalMessages;
    }

    public void setTotalMessages(List<Integer> totalMessages) {
        this.totalMessages = totalMessages;
    }

    public Integer getTotalGroups() {
        return totalGroups;
    }

    public void setTotalGroups(Integer totalGroups) {
        this.totalGroups = totalGroups;
    }

    public Set<StudyGroupDTO> getGroup() {
        return group;
    }

    public void setGroup(Set<StudyGroupDTO> group) {
        this.group = group;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(LocalDateTime todayDate) {
        this.todayDate = todayDate;
    }

    public LocalDateTime getLastDate() {
        return lastDate;
    }

    public void setLastDate(LocalDateTime lastDate) {
        this.lastDate = lastDate;
    }

    public DashboardMessageDTO() {
        super();
        // TODO Auto-generated constructor stub
    }

}
