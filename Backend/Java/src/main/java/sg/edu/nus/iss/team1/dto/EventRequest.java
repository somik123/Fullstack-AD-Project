package sg.edu.nus.iss.team1.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class EventRequest implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    private String name;
    private String description;
    private String username;
    // date json format(yyyy-MM-dd HH:mm:ss)
    private LocalDateTime createTime;
    private String groupName;
    // date json format(yyyy-MM-dd HH:mm:ss)
    private LocalDateTime eventTime;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public EventRequest() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return "EventRequest [name=" + name + ", description=" + description + ", username=" + username
                + ", createTime=" + createTime + ", groupName=" + groupName + ", eventTime=" + eventTime + "]";
    }

}
