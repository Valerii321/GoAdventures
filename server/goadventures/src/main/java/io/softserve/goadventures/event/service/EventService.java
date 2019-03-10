package io.softserve.goadventures.event.service;

import io.softserve.goadventures.event.model.Event;
import io.softserve.goadventures.event.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService{
    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }

    public Event getEventById(int id){
        return eventRepository.findById(id);
    }

    public Event getEventByTopic(String topic){
        return eventRepository.findByTopic(topic);
    }

    public void addEvent(Event newEvent) {eventRepository.save(newEvent);}

    public void updateEvent(Event event) {
        eventRepository.save(event);
    }

    public Iterable<Event> getAllEvents() {
        return eventRepository.findAll();
    }
}
