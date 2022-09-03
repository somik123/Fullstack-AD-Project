package sg.edu.nus.iss.team1.dto;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserDTO {
    private Integer id;
    @NotBlank(message = "Username is mandatory")
    private String username;
    @NotBlank(message = "Password is mandatory")
    private String password;
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Please make sure it's email account")
    private String email;
    private String photo;
    private List<String> interest;


    public UserDTO(Integer id, @NotBlank(message = "Username is mandatory") String username,
                   @NotBlank(message = "Password is mandatory") String password,
                   @NotBlank(message = "Name is mandatory") String name,
                   @NotBlank(message = "Email is mandatory") @Email(message = "Please make sure it's email account") String email,
                   String photo, List<String> interest) {
        super();
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.photo = photo;
        this.interest = interest;
    }

    public String getUsername() {
        return username;
    }

    public UserDTO(@NotBlank(message = "Username is mandatory") String username,
                   @NotBlank(message = "Password is mandatory") String password,
                   @NotBlank(message = "Name is mandatory") String name,
                   @NotBlank(message = "Email is mandatory") @Email(message = "Please make sure it's email account") String email,
                   String photo) {
        super();
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.photo = photo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public UserDTO() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return "UserDTO [username=" + username + ", password=" + password + ", name=" + name + ", email=" + email
                + ", photo=" + photo + "]";
    }

    public List<String> getInterest() {
        return interest;
    }

    public void setInterest(List<String> interest) {
        this.interest = interest;
    }

}
