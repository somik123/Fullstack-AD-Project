package sg.edu.nus.iss.team1.service;

import java.util.List;

import sg.edu.nus.iss.team1.domain.Event;
import sg.edu.nus.iss.team1.domain.User;
import sg.edu.nus.iss.team1.dto.EventRequest;
import sg.edu.nus.iss.team1.dto.EventResponse;

public interface EventService {

    public List<Event> getAllEvents();

    public void saveEvent(Event event);

    public Event findById(Integer Id);

    public List<Event> findByName(String name);

    public void deleteById(Integer id);

    EventResponse savenewEvent(EventRequest requestPayload, User creatorUser);

    EventResponse updateEvent(EventRequest requestPayload, Event existingEvent);

    EventResponse joinEvent(User joinUser, Integer eventId);

}
