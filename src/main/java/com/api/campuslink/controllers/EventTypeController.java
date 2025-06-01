package com.api.campuslink.controllers;

import com.api.campuslink.helpers.Result;
import com.api.campuslink.models.entities.EventType;
import com.api.campuslink.services.EventTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventtype")
@Slf4j
public class EventTypeController {

    @Autowired
    private EventTypeService eventTypeService;

    @PostMapping("/add")
    public ResponseEntity<?> insertEventType(@RequestBody EventType req) {
        log.info("Got request to add new event type");
        EventType eventType = EventType.builder()
                .eventCode(req.getEventCode())
                .name(req.getName())
                .description(req.getDescription())
                .build();

        Result<EventType> result = eventTypeService.addEventType(eventType);

        if (!result.isSuccess()) {
            return new ResponseEntity<>("Unable to save event type " + eventType.getName() + " due to " + result.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Event type " + eventType.getName() + " saved succesfully.", HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllEventType(@RequestParam(defaultValue = "id") String sortBy,
                                             @RequestParam(defaultValue = "true") boolean asc) {
        log.info("Fetching all the event type.");
        Result<List<EventType>> result = this.eventTypeService.getAllEventType(sortBy, asc);

        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getData(), HttpStatus.OK);
        }
     log.info("hello world help me in this situation");return new ResponseEntity<>("Unable to fetch the event types", HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
