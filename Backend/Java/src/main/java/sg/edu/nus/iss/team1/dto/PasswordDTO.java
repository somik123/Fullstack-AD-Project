package sg.edu.nus.iss.team1.dto;

public class PasswordDTO {
    private Integer userId;
    private String currentPassword;
    private String updatePassword;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getUpdatePassword() {
        return updatePassword;
    }

    public void setUpdatePassword(String updatePassword) {
        this.updatePassword = updatePassword;
    }

    public PasswordDTO() {
        super();
        // TODO Auto-generated constructor stub
    }


}
