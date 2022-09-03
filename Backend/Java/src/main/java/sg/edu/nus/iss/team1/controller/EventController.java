package sg.edu.nus.iss.team1.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.team1.domain.Event;
import sg.edu.nus.iss.team1.domain.StudyGroup;
import sg.edu.nus.iss.team1.domain.User;
import sg.edu.nus.iss.team1.dto.EventRequest;
import sg.edu.nus.iss.team1.dto.EventResponse;
import sg.edu.nus.iss.team1.service.EventService;
import sg.edu.nus.iss.team1.service.RoleService;
import sg.edu.nus.iss.team1.service.StudyGroupService;
import sg.edu.nus.iss.team1.service.UserService;
import sg.edu.nus.iss.team1.util.MLApi;

@RestController
@RequestMapping(path = "/event", produces = MediaType.APPLICATION_JSON_VALUE)
public class EventController {
    @Autowired
    EventService eventService;

    @Autowired
    UserService userService;

    @Autowired
    StudyGroupService studyGroupService;

    @Autowired
    RoleService roleService;

    @GetMapping("/all")
    public ResponseEntity<?> findAllEvents() {
        EventResponse eventResponse;
        List<Event> eventList = eventService.getAllEvents();
        List<EventResponse> eventResponses;

        if (!eventList.isEmpty()) {
            eventResponses = new ArrayList<EventResponse>();
            for (Event event : eventList) {
                List<String> participantNames = new ArrayList<>();
                List<String> participantPhotos = new ArrayList<>();
                eventResponse = new EventResponse();
                eventResponse.setId(event.getId());
                eventResponse.setName(event.getName());
                eventResponse.setDescription(event.getDescription());
                eventResponse.setUserName(event.getCreator().getUsername());
                eventResponse.setCreateTime(event.getCreateTime());
                eventResponse.setGroupName(event.getGroup().getName());
                eventResponse.setGroupId(event.getGroup().getId());
                eventResponse.setEventTime(event.getEventTime());
                for (User user : event.getGroup().getParticipants()) {
                    participantNames.add(user.getUsername());
                    participantPhotos.add(user.getPhoto());

                }
                eventResponse.setParticipantName(participantNames);
                eventResponse.setParticipantPhoto(participantPhotos);
                eventResponses.add(eventResponse);
            }
            return new ResponseEntity<>(eventResponses, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No event data exist", HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findEventById(@PathVariable Integer id) {

        EventResponse eventResponse;
        Event existData = eventService.findById(id);
        if (existData != null) {
            List<String> participantNames = new ArrayList<>();
            List<String> participantPhotos = new ArrayList<>();
            eventResponse = new EventResponse();
            eventResponse.setId(existData.getId());
            eventResponse.setName(existData.getName());
            eventResponse.setDescription(existData.getDescription());
            eventResponse.setUserName(existData.getCreator().getUsername());
            eventResponse.setCreateTime(existData.getCreateTime());
            eventResponse.setGroupName(existData.getGroup().getName());
            eventResponse.setGroupId(existData.getGroup().getId());
            for (User user : existData.getGroup().getParticipants()) {
                participantNames.add(user.getUsername());
                participantPhotos.add(user.getPhoto());

            }
            eventResponse.setParticipantName(participantNames);
            eventResponse.setParticipantPhoto(participantPhotos);
            eventResponse.setEventTime(existData.getEventTime());
            return new ResponseEntity<>(eventResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No event with id " + id + " exist", HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> findEventByName(@PathVariable String name) {
        List<EventResponse> eventResponseList = new ArrayList<>();
        List<Event> eventList = eventService.findByName(name);
        List<String> participantNames = new ArrayList<>();
        List<String> participantPhotos = new ArrayList<>();
        if (eventList.size() > 0) {
            for (Event existData : eventList) {
                EventResponse eventResponse = new EventResponse();
                eventResponse.setId(existData.getId());
                eventResponse.setName(existData.getName());
                eventResponse.setDescription(existData.getDescription());
                eventResponse.setUserName(existData.getCreator().getUsername());
                eventResponse.setCreateTime(existData.getCreateTime());
                eventResponse.setGroupName(existData.getGroup().getName());
                eventResponse.setGroupId(existData.getGroup().getId());
                eventResponse.setEventTime(existData.getEventTime());
                for (User user : existData.getGroup().getParticipants()) {
                    participantNames.add(user.getUsername());
                    participantPhotos.add(user.getPhoto());

                }
                eventResponse.setParticipantName(participantNames);
                eventResponse.setParticipantPhoto(participantPhotos);
                eventResponseList.add(eventResponse);
            }
            return new ResponseEntity<>(eventResponseList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No event with name " + name + " exist", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createEvent(@RequestBody EventRequest requestPayload) {
        User creatorUser = userService.findByUsername(requestPayload.getUsername());
        if (creatorUser != null) {
            StudyGroup newGroup = studyGroupService.findByName(requestPayload.getGroupName());
            if (newGroup != null) {
                return new ResponseEntity<>(
                        "Group name " + requestPayload.getGroupName() + " already exist.Cannot create event.",
                        HttpStatus.BAD_REQUEST);
            } else {
                EventResponse eventRes = eventService.savenewEvent(requestPayload, creatorUser);
                updateUserBookRecommendations(creatorUser, requestPayload.getName());
                return new ResponseEntity<>(eventRes, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("You are not allow to create event because your username cannot be found",
                    HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/{id}")
    ResponseEntity<?> updateEvent(@RequestBody EventRequest requestPayload, @PathVariable Integer id) {
        Event existData = eventService.findById(id);
        if (existData != null) {
            return new ResponseEntity<>(eventService.updateEvent(requestPayload, existData), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No Event with id " + id + " exist", HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteEvent(@PathVariable Integer id) {
        eventService.deleteById(id);
        return new ResponseEntity<>("Event Deleted", HttpStatus.OK);
    }

    @PostMapping("/{userId}/{id}")
    public ResponseEntity<?> joinEvent(@PathVariable Integer userId, @PathVariable Integer id) {
        User existingUser = userService.findById(userId);
        if (existingUser != null) {
            EventResponse eventResponse = eventService.joinEvent(existingUser, id);
            if (eventResponse.getId() == null) {
                return new ResponseEntity<>("No event found with event Id " + id,
                        HttpStatus.NOT_FOUND);
            } else {
                updateUserBookRecommendations(existingUser, eventResponse.getName());
                return new ResponseEntity<>(eventResponse, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("You are not allow to join event because your username cannot be found",
                    HttpStatus.NOT_FOUND);
        }
    }

    private void updateUserBookRecommendations(User user, String eventName) {
        try {
            int maxSleep = 10; // seconds
            MLApi mlApi = new MLApi();

            System.out.println(user.getInterest());
            System.out.println(eventName);

            mlApi.recommendBooks(user.getInterest(), eventName);
            for (int i = 0; i < maxSleep * 10 && mlApi.getBkThread().isAlive(); i++) {
                try {
                    Thread.sleep(100); // Wait 0.1s at a time for background thread to complete
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
            String reply = mlApi.getMlReply();
            user.set_bookOrdering(reply);
            user.set_eventLatest(eventName);
            userService.saveUser(user);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
