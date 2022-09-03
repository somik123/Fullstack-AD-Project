package edu.iss.nus.group1.studybuddy.dto;

import java.io.Serializable;

public class FileResponseDTO implements Serializable {

    private String message;
    private String url;

    public FileResponseDTO(String message, String url) {
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

    public FileResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
