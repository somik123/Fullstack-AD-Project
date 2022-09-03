package sg.edu.nus.iss.team1.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.team1.domain.Event;
import sg.edu.nus.iss.team1.domain.StudyGroup;
import sg.edu.nus.iss.team1.domain.User;
import sg.edu.nus.iss.team1.dto.EventRequest;
import sg.edu.nus.iss.team1.dto.EventResponse;
import sg.edu.nus.iss.team1.repository.EventRepository;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    StudyGroupService groupService;

    public List<Event> getAllEvents() {
        return eventRepository.findAllEventOrderByDateTimeDesc();
    }

    public void saveEvent(Event event) {
        eventRepository.save(event);
    }

    public Event findById(Integer Id) {
        return eventRepository.findById(Id).get();
    }

    public List<Event> findByName(String name) {
        return eventRepository.findByName(name);
    }

    public void deleteById(Integer id) {
        eventRepository.deleteById(id);
    }

    @Override
    public EventResponse savenewEvent(EventRequest requestPayload, User creatorUser) {
        LocalDateTime parsedDate = localdatetimeFormat();
        Event newEvent = new Event();
        newEvent.setName(requestPayload.getName());
        newEvent.setDescription(requestPayload.getDescription());
        newEvent.setCreator(creatorUser);
        newEvent.setCreateTime(parsedDate);
        newEvent.setEventTime(requestPayload.getEventTime());

        List<User> participants = new ArrayList<>();
        participants.add(creatorUser);
        StudyGroup newGroup = new StudyGroup();
        newGroup.setName(requestPayload.getGroupName());
        newGroup.setIsPrivate(false);
        newGroup.setCreateDate(parsedDate);
        newGroup.setCreator(creatorUser);
        newGroup.setParticipants(participants);
        groupService.saveStudyGroup(newGroup);
        newEvent.setGroup(newGroup);
        eventRepository.save(newEvent);

        EventResponse eventRes = new EventResponse();
        eventRes.setId(newEvent.getId());
        eventRes.setName(newEvent.getName());
        eventRes.setDescription(newEvent.getDescription());
        eventRes.setUserName(newEvent.getCreator().getUsername());
        List<String> participantNames = new ArrayList<>();
        List<String> participantPhotos = new ArrayList<>();
        for (User user : participants) {
            participantNames.add(user.getUsername());
            participantPhotos.add(user.getPhoto());

        }
        eventRes.setParticipantName(participantNames);
        eventRes.setParticipantPhoto(participantPhotos);
        eventRes.setEventTime(newEvent.getEventTime());
        eventRes.setCreateTime(newEvent.getCreateTime());
        eventRes.setGroupName(newEvent.getGroup().getName());
        eventRes.setGroupId(newEvent.getGroup().getId());
        return eventRes;
    }

    @Override
    public EventResponse updateEvent(EventRequest requestPayload, Event existingEvent) {
        LocalDateTime parsedDate = localdatetimeFormat();
        StudyGroup existingGroup = groupService.findById(existingEvent.getGroup().getId());

        existingEvent.setName(requestPayload.getName());
        existingEvent.setCreateTime(parsedDate);
        existingEvent.setDescription(requestPayload.getDescription());
        existingEvent.setEventTime(requestPayload.getEventTime());
        eventRepository.save(existingEvent);

        EventResponse eventRes = new EventResponse();
        eventRes.setId(existingEvent.getId());
        eventRes.setName(existingEvent.getName());
        eventRes.setDescription(existingEvent.getDescription());
        eventRes.setUserName(existingEvent.getCreator().getUsername());
        List<String> participantNames = new ArrayList<>();
        List<String> participantPhotos = new ArrayList<>();
        for (User user : existingGroup.getParticipants()) {
            participantNames.add(user.getUsername());
            participantPhotos.add(user.getPhoto());

        }
        eventRes.setParticipantName(participantNames);
        eventRes.setParticipantPhoto(participantPhotos);
        eventRes.setEventTime(existingEvent.getEventTime());
        eventRes.setCreateTime(parsedDate);
        eventRes.setGroupName(existingEvent.getGroup().getName());
        eventRes.setGroupId(existingEvent.getGroup().getId());
        return eventRes;
    }

    @Override
    public EventResponse joinEvent(User joinUser, Integer eventId) {
        EventResponse eventResponse = new EventResponse();
        if (eventRepository.findById(eventId).isPresent()) {
            Event existingEvent = eventRepository.findById(eventId).get();
            StudyGroup existingGroup = groupService.findByName(existingEvent.getGroup().getName());
            if (existingGroup != null) {
                List<User> participants = (List<User>) existingGroup.getParticipants();
                if (!groupService.checkparticipants(participants, joinUser)) {
                    participants.add(joinUser);
                    existingGroup.setParticipants(participants);
                    groupService.saveStudyGroup(existingGroup);
                }
                eventResponse.setId(existingEvent.getId());
                eventResponse.setName(existingEvent.getName());
                eventResponse.setDescription(existingEvent.getDescription());
                eventResponse.setUserName(existingEvent.getCreator().getUsername());
                List<String> participantNames = new ArrayList<>();
                for (User user : existingGroup.getParticipants()) {
                    participantNames.add(user.getUsername());
                }
                eventResponse.setParticipantName(participantNames);
                eventResponse.setEventTime(existingEvent.getEventTime());
                eventResponse.setCreateTime(existingEvent.getCreateTime());
                eventResponse.setGroupName(existingEvent.getGroup().getName());
                eventResponse.setGroupId(existingEvent.getGroup().getId());
            }
        }
        return eventResponse;
    }

    public LocalDateTime localdatetimeFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localTime = LocalDateTime.now();
        String text = localTime.format(formatter);
        return LocalDateTime.parse(text, formatter);
    }
}
