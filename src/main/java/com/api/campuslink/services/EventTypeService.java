package com.api.campuslink.services;

import com.api.campuslink.dao.EventTypeRepository;
import com.api.campuslink.helpers.Result;
import com.api.campuslink.models.dto.EventTypeRequestDTO;
import com.api.campuslink.models.entities.EventType;
import com.api.campuslink.utils.EventTypeComprator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class EventTypeService {

    @Autowired
    private EventTypeRepository eventTypeRepository;

    public Result<EventType> addEventType(EventTypeRequestDTO eventTypeRequestDTO) {
        try {
            log.info("Saving event type");
            EventType eventType = buildEventType(eventTypeRequestDTO);
            EventType savedEventType = this.eventTypeRepository.save(eventType);
            return Result.success(savedEventType);
        } catch (Exception e) {
            log.debug("Got error while saving event type");
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }


    public Result<List<EventType>> getAllEventType(String sortBy, boolean ascending) {

        List<EventType> eventTypeList = new ArrayList<>();
        if (Objects.equals(sortBy, "eventCode")) {
            eventTypeList = (List<EventType>) this.eventTypeRepository.findAll();
            eventTypeList.sort(new EventTypeComprator());
        } else {
            Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            eventTypeList = this.eventTypeRepository.findAll(sort);
        }

        if (eventTypeList.isEmpty()) {
            log.info("No event types found");
            return Result.error("No event types found", HttpStatus.FOUND);
        }
        return Result.success(eventTypeList);
    }

    public Result<EventType> updateEventType(EventTypeRequestDTO eventTypeRequestDTO) {
        try {
            log.info("Updating event type with code: {}", eventTypeRequestDTO.getEventCode());
            EventType existingEventType = this.eventTypeRepository.findByEventCode(eventTypeRequestDTO.getEventCode());
            if (existingEventType == null) {
                log.error("Event type with code {} not found", eventTypeRequestDTO.getEventCode());
                return Result.error("Event type with code " + eventTypeRequestDTO.getEventCode() + " not found", HttpStatus.NOT_FOUND);
            }
            EventType updatedEventType = buildEventType(eventTypeRequestDTO);
            updatedEventType.setId(existingEventType.getId());
            this.eventTypeRepository.save(updatedEventType);
            return Result.success(updatedEventType);
        } catch (Exception e) {
            log.debug("Got error while updating event type");
            log.error(e.getMessage());
            return Result.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Result<String> deleteEventType(Long id) {
        try {
            log.info("Deleting event type with code: {}", id);
            EventType eventType = this.eventTypeRepository.findById(id).orElse(null);
            if (eventType == null) {
                log.error("Event type with code {} not found", id);
                return Result.error("Event type with code " + id + " not found", HttpStatus.NOT_FOUND);
            }
            this.eventTypeRepository.delete(eventType);
            return Result.success("Event type with code " + id + " deleted successfully.");
        } catch (Exception e) {
            log.debug("Got error while deleting event type");
            log.error(e.getMessage());
            return Result.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private EventType buildEventType(EventTypeRequestDTO eventTypeRequestDTO){
        return EventType.builder()
                .eventCode(eventTypeRequestDTO.getEventCode())
                .name(eventTypeRequestDTO.getName())
                .description(eventTypeRequestDTO.getDescription())
                .build();
    }

}
