package sg.edu.nus.iss.team1.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class StudyGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Integer Id;

    public StudyGroup(String name, Boolean isPrivate, LocalDateTime createDate, User creator,
                      Collection<User> participants) {
        super();
        this.name = name;
        this.isPrivate = isPrivate;
        this.createDate = createDate;
        this.creator = creator;
        this.participants = participants;
    }

    private String name;
    private Boolean isPrivate;
    private LocalDateTime createDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;
    @ManyToMany
    @JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
    private Collection<User> participants;
    private boolean individualChat;
    private LocalDateTime lastactiveTime;


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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Collection<User> getParticipants() {
        return participants;
    }

    public void setParticipants(Collection<User> participants) {
        this.participants = participants;
    }


    public boolean isIndividualChat() {
        return individualChat;
    }

    public void setIndividualChat(boolean individualChat) {
        this.individualChat = individualChat;
    }


    public LocalDateTime getLastactiveTime() {
        return lastactiveTime;
    }

    public void setLastactiveTime(LocalDateTime lastactiveTime) {
        this.lastactiveTime = lastactiveTime;
    }

    public StudyGroup() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return "StudyGroup{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", isPrivate=" + isPrivate +
                ", createDate=" + createDate +
                ", creator=" + creator +
                ", participants=" + participants +
                '}';
    }
}
