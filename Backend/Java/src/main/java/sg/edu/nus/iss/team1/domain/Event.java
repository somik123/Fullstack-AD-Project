package sg.edu.nus.iss.team1.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;


@Entity
public class Event implements Serializable {
    private static final long serialVersionUID = 7630017132244736425L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private String name;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;

    public Event(String name, String description, User creator, LocalDateTime createTime, StudyGroup group, LocalDateTime eventTime) {
        super();
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.createTime = createTime;
        this.group = group;
        this.eventTime = eventTime;
    }

    private LocalDateTime createTime;
    @OneToOne
    private StudyGroup group;
    private LocalDateTime eventTime;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public StudyGroup getGroup() {
        return group;
    }

    public void setGroup(StudyGroup group) {
        this.group = group;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public Event() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return "Event [Id=" + Id + ", name=" + name + ", description=" + description + ", creator=" + creator
                + ", createTime=" + createTime + ", group=" + group + ", eventTime="
                + eventTime + "]";
    }

}
