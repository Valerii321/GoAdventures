package io.softserve.goadventures.controllers;

import io.softserve.goadventures.dto.EventDTO;
import io.softserve.goadventures.enums.EventStatus;
import io.softserve.goadventures.errors.ErrorMessageManager;
import io.softserve.goadventures.errors.UserNotFoundException;
import io.softserve.goadventures.models.Category;
import io.softserve.goadventures.models.Event;
import io.softserve.goadventures.models.Gallery;
import io.softserve.goadventures.models.User;
import io.softserve.goadventures.repositories.CategoryRepository;
import io.softserve.goadventures.repositories.EventRepository;
import io.softserve.goadventures.repositories.GalleryRepository;
import io.softserve.goadventures.services.EventDtoBuilder;
import io.softserve.goadventures.services.EventService;
import io.softserve.goadventures.services.JWTService;
import io.softserve.goadventures.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("event")
public class EventController {
  private Logger logger = LoggerFactory.getLogger(EventController.class);
  private final EventService eventService;
  private final EventRepository eventRepository;
  private final CategoryRepository categoryRepository;
  private final EventDtoBuilder eventDtoBuilder;
  private final JWTService jwtService;
  private final UserService userService;
  private final ModelMapper modelMapper;

  @Autowired
  public EventController(EventService eventService, EventRepository eventRepository,
                         CategoryRepository categoryRepository, GalleryRepository galleryRepository, EventDtoBuilder eventDtoBuilder,
                         UserService userService, JWTService jwtService, ModelMapper modelMapper) {
    this.eventService = eventService;
    this.eventRepository = eventRepository;
    this.categoryRepository = categoryRepository;
    this.eventDtoBuilder = eventDtoBuilder;
    this.jwtService = jwtService;
    this.userService = userService;
    this.modelMapper = modelMapper;


  }

  @PostMapping("/create")
  public ResponseEntity<String> createEvent(@RequestHeader(value = "Authorization") String token,
                                            @RequestBody EventDTO event) {
    Event mappedEvent = modelMapper.map(event, Event.class);
    Category category = categoryRepository.findByCategoryName(event.getCategory());
    System.out.println(mappedEvent.getTopic());
    mappedEvent.setCategory(category);
    mappedEvent.setStatusId(EventStatus.OPENED.getEventStatus());
    Gallery gallery;
    if (event.getGallery() != null) {
      gallery = modelMapper.map(event.getGallery(), Gallery.class);
      gallery.setEventId(mappedEvent);
      mappedEvent.setGallery(gallery);
    }
    try {
      eventService.addEvent(mappedEvent, token);
      LoggerFactory.getLogger("Create Event Controller: ")
              .info(userService.getUserByEmail(jwtService.parseToken(token)).toString());
      eventService.addEvent(mappedEvent, token);
    } catch (UserNotFoundException e) {
      e.printStackTrace();
    }
    return ResponseEntity.ok("Event created");
  }

  @PostMapping("/close")
  public ResponseEntity<String> closeEvent(@RequestHeader(value = "Authorization") String token,
                                           @RequestHeader(value = "EventId") int eventId) throws UserNotFoundException {
    Event event = eventService.getEventById(eventId);
    User user = userService.getUserByEmail(jwtService.parseToken(token));
    if (eventService.closeEvent(user, event)) {
      return ResponseEntity.ok("Event closed");
    } else {
      return ResponseEntity.badRequest().body("Close doesn't work");
    }
  }

  @PostMapping("/open")
  public ResponseEntity<String> openEvent(@RequestHeader(value = "Authorization") String token,
                                          @RequestHeader(value = "EventId") int eventId) throws UserNotFoundException {
    Event event = eventService.getEventById(eventId);
    User user = userService.getUserByEmail(jwtService.parseToken(token));
    if (eventService.openEvent(user, event)) {
      return ResponseEntity.ok("Event opened");
    } else {
      return ResponseEntity.badRequest().body("Open doesn't work");
    }
  }

  @PostMapping("/category")
  public ResponseEntity<String> createCategory(@RequestBody Category category) {
    category.setEvents(null);
    categoryRepository.save(category);

    HttpHeaders httpHeaders = new HttpHeaders();
    return ResponseEntity.ok().headers(httpHeaders).body("Category created");
  }

  @GetMapping("/all")
  public ResponseEntity<?> getAllEvents(@RequestParam(value = "search", required = false) String search,
                                        @PageableDefault(size = 15, sort = "id") Pageable eventPageable) {

    Page<Event> eventsPage = (search == null) ? eventService.getAllEvents(eventPageable)
            : eventService.getAllEventBySearch(eventPageable, search);

    if (eventsPage != null) {
      int nextPageNum = eventsPage.getNumber() + 1;
      UriComponents uriComponentsBuilder = UriComponentsBuilder.newInstance().path("/event/all")
              .query("page={keyword}").buildAndExpand(nextPageNum);
      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.set("nextpage", uriComponentsBuilder.toString());
      Slice<EventDTO> eventDTOSlice = eventDtoBuilder.convertToDto(eventsPage);
      logger.info("Event converted to dto", eventDTOSlice.getContent());
      return new ResponseEntity<Slice<EventDTO>>(eventDTOSlice, httpHeaders, HttpStatus.OK);
    } else {
      return ResponseEntity.badRequest()
              .body(new ErrorMessageManager("Server error, try again later", "Pageable error"));
    }
  }

  @PutMapping("update/{eventId}")
  public ResponseEntity<?> updateEvent(@PathVariable("eventId") int eventId, @RequestBody EventDTO updatedEvent) {
    try {
      Event event = eventService.getEventById(eventId);
      if (event != null) {
        modelMapper.map(updatedEvent, event);
        return ResponseEntity.ok().body(modelMapper.map(eventService.updateEvent(event), EventDTO.class));
      } else {
        throw new IOException("Event does not exist");
      }
    } catch (IOException error) {
      logger.debug(error.toString());
      return ResponseEntity.status(500)
              .body(new ErrorMessageManager("Server error, try again later", error.toString()));
    }
  }

  @GetMapping("/allCategory")
  public Iterable<Category> getAllCategory() {
    return categoryRepository.findAll();
  }

  @GetMapping("/categ/{categoryId}")
  public String getAllCategory(@PathVariable(value = "categoryId") int categoryId) {
        Category category = categoryRepository.findByEventsId(categoryId);
        return category.getCategoryName();
    }

  @GetMapping("/category/{categoryId}")
  public Page<Event> getAllEventsByCategoryId(@PathVariable(value = "categoryId") int eventId,
                                              @PageableDefault(size = 15, sort = "id")Pageable pageable) {
    return eventRepository.findByCategoryId(eventId, pageable);
  }

  @GetMapping("/{eventId}")
  public Event getEvent(@PathVariable(value = "eventId") int eventId) {
    return eventService.getEventById(eventId);
  }

  @DeleteMapping("delete")
  public ResponseEntity<?> deleteEventOwner(@RequestHeader(value = "Authorization") String token,
                                            @RequestHeader(value = "EventId") int eventId) throws UserNotFoundException {
    Event event = eventService.getEventById(eventId);
    User user = userService.getUserByEmail(jwtService.parseToken(token));
    if (eventService.delete(user, event)) {
      return ResponseEntity.ok("Event deleted");
    } else {
      return ResponseEntity.badRequest().body("Delete doesn't work");
    }
  }

  @PostMapping("isOwner")
  public ResponseEntity<?> isOwner(@RequestHeader(value = "Authorization") String token,
                                   @RequestHeader(value = "EventId") int eventId) throws UserNotFoundException {
    LoggerFactory.getLogger("IS OWNER EVENT").info("Event ID is : " + eventId + " , OwnerId is : "
            + userService.getUserByEmail(jwtService.parseToken(token)).getId());
    Event event = eventService.getEventById(eventId);
    User user = userService.getUserByEmail(jwtService.parseToken(token));

    return user.getId() == event.getOwner().getId() ? ResponseEntity.ok().body("IS OWNER")
            : ResponseEntity.badRequest().body("IS NOT OWNER");
  }
}
