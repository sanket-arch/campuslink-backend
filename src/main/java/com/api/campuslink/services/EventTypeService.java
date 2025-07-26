package com.api.campuslink.services;

import com.api.campuslink.dao.EventTypeRepository;
import com.api.campuslink.helpers.Result;
import com.api.campuslink.models.dto.EventTypeRequestDTO;
import com.api.campuslink.models.entities.EventType;
import com.api.campuslink.utils.EventTypeComprator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

        if (Objects.equals(sortBy, "eventCode")) {
            List<EventType> eventTypeList = (List<EventType>) this.eventTypeRepository.findAll();
            eventTypeList.sort(new EventTypeComprator());
        } else {
            Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            List<EventType> eventTypeList = this.eventTypeRepository.findAll(sort);
        }

        return null;
    }

    private EventType buildEventType(EventTypeRequestDTO eventTypeRequestDTO){
        return EventType.builder()
                .eventCode(eventTypeRequestDTO.getEventCode())
                .name(eventTypeRequestDTO.getName())
                .description(eventTypeRequestDTO.getDescription())
                .build();
    }

}
