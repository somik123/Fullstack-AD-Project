package sg.edu.nus.iss.team1.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class RequestBlockerController {

    protected final String blockMessage = "{\"Error\": \"You do not have permission to access this page.\"}";

    @RequestMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public String defaultBlocker() {
        return blockMessage;
    }

    @RequestMapping(path = "/interests", produces = MediaType.APPLICATION_JSON_VALUE)
    public String interestsBlocker() {
        return blockMessage;
    }

    @RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public String usersBlocker() {
        return blockMessage;
    }

    @RequestMapping(path = "/studyGroups", produces = MediaType.APPLICATION_JSON_VALUE)
    public String studyGroupsBlocker() {
        return blockMessage;
    }

    @RequestMapping(path = "/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public String messagesBlocker() {
        return blockMessage;
    }

    @RequestMapping(path = "/books", produces = MediaType.APPLICATION_JSON_VALUE)
    public String booksBlocker() {
        return blockMessage;
    }

    @RequestMapping(path = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public String profileBlocker() {
        return blockMessage;
    }

    @RequestMapping(path = "/roles", produces = MediaType.APPLICATION_JSON_VALUE)
    public String rolesBlocker() {
        return blockMessage;
    }

    @RequestMapping(path = "/events", produces = MediaType.APPLICATION_JSON_VALUE)
    public String eventsBlocker() {
        return blockMessage;
    }
}
