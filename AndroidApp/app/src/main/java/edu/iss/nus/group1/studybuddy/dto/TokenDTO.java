package edu.iss.nus.group1.studybuddy.dto;

public class TokenDTO {
    protected Integer userId;
    protected String userName;
    protected String token;
    protected String displayName;
    protected String photo;

    public TokenDTO() {
        super();
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getToken() {
        return token;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPhoto() {
        return photo;
    }

    @Override
    public String toString() {
        return "TokenDTO{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", token='" + token + '\'' +
                ", displayName='" + displayName + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
