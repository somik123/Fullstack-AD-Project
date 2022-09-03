package sg.edu.nus.iss.team1.exception;

import java.util.List;

public class ErrorResponse {
    public ErrorResponse(String message, List<String> details) {
        super();
        this.message = message;
        this.details = details;
    }

    //General error message about nature of error
    public String message;

    //Specific errors in API request processing
    public List<String> details;
}
