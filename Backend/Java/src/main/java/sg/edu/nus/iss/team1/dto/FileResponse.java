package sg.edu.nus.iss.team1.dto;

public class FileResponse {
    private String message;
    private String url;

    public FileResponse(String message, String url) {
        super();
        this.message = message;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public FileResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
