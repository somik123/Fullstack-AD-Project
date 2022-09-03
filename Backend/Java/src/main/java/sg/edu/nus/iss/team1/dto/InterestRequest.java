package sg.edu.nus.iss.team1.dto;

import java.io.Serializable;
import java.util.List;

public class InterestRequest implements Serializable {

    private static final long serialVersionUID = -8091871191924046844L;
    private Integer userId;

    private List<Integer> interests;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<Integer> getInterests() {
        return interests;
    }

    public void setInterests(List<Integer> interests) {
        this.interests = interests;
    }

    public InterestRequest() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return "InterestRequest{" +
                "userId=" + userId +
                ", interests=" + interests +
                '}';
    }
}
