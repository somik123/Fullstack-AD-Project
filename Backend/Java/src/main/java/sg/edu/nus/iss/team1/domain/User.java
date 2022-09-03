package sg.edu.nus.iss.team1.domain;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer Id;
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "Username is mandatory")
    private String username;
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Please make sure it's email account")
    private String email;
    @NotBlank(message = "Password is mandatory")
    private String password;
    private String photo;
    private Integer Karma;
    /**
     * Contains the Id (1-382) of all books and the order on how they should appear
     * when the user selects the Textbook screen e.g. "180,75,301,1....2,381,382"
     * -First book to appear in the screen is id 182, followed by 75 so on.
     */
    @Size(max = 1500)
    private String _bookOrdering;
    /**
     * Contains the title of the latest event user has joined. Is used for textbook
     * recommendations
     */
    private String _eventLatest;
    @ManyToMany
    private Collection<Interest> interest;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role userRole;////

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getKarma() {
        return Karma;
    }

    public void setKarma(Integer karma) {
        Karma = karma;
    }

    public Collection<Interest> getInterest() {
        return interest;
    }

    public void setInterest(Collection<Interest> interest) {
        this.interest = interest;
    }

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    public String get_bookOrdering() {
        return _bookOrdering;
    }

    public void set_bookOrdering(String _bookOrdering) {
        this._bookOrdering = _bookOrdering;
    }

    public String get_eventLatest() {
        return _eventLatest;
    }

    public void set_eventLatest(String _eventLatest) {
        this._eventLatest = _eventLatest;
    }

    public User() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return "User [Id=" + Id + ", name=" + name + ", username=" + username + ", email=" + email + ", password="
                + password + ", photo=" + photo + ", Karma=" + Karma + ", _bookOrdering=" + _bookOrdering
                + ", _eventLatest=" + _eventLatest + ", interest=" + interest + ", userRole=" + userRole + "]";
    }

}
