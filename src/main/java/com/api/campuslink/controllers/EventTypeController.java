package com.api.campuslink.controllers;

import com.api.campuslink.helpers.Result;
import com.api.campuslink.models.dto.EventTypeRequestDTO;
import com.api.campuslink.models.entities.EventType;
import com.api.campuslink.services.EventTypeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventType")
@Slf4j
public class EventTypeController {

    @Autowired
    private EventTypeService eventTypeService;

    @PostMapping("/add")
    public ResponseEntity<?> insertEventType(@RequestBody @Valid EventTypeRequestDTO eventTypeRequestDTO) {
        log.info("Got request to add new event type");

        Result<EventType> result = eventTypeService.addEventType(eventTypeRequestDTO);

        if (!result.isSuccess()) {
            return new ResponseEntity<>("Unable to save event type " + eventTypeRequestDTO.getName() + " due to " + result.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Event type " + eventTypeRequestDTO.getName() + " saved succesfully.", HttpStatus.OK);
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
