package sg.edu.nus.iss.team1.jwt;

import java.io.Serializable;

public class JwtResponse implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;
    private final int userId;
    private final String userName;
    private final String displayName;
    private final String photo;

    public JwtResponse(String jwttoken, int userId, String userName, String displayName, String photo) {
        this.jwttoken = jwttoken;
        this.userId = userId;
        this.userName = userName;
        this.displayName = displayName;
        this.photo = photo;
    }

    public String getToken() {
        return this.jwttoken;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getPhoto() {
        return this.photo;
    }
}
