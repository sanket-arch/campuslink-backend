package com.api.campuslink.services;

import com.api.campuslink.dao.EntityTypeRepository;
import com.api.campuslink.helpers.Result;
import com.api.campuslink.models.entities.EventType;
import com.api.campuslink.utils.EventTypeComprator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class EventTypeService {

    @Autowired
    private EntityTypeRepository entityTypeRepository;

    public Result<EventType> addEventType(EventType eventType) {
        try {
            log.info("Saving event type");
            EventType savedEventType = this.entityTypeRepository.save(eventType);
            return Result.success(savedEventType);

        } catch (Exception e) {
            log.debug("Got error while saving event type");
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }


    public Result<List<EventType>> getAllEventType(String sortBy, boolean ascending) {

        if (Objects.equals(sortBy, "eventCode")) {
            List<EventType> eventTypeList = (List<EventType>) this.entityTypeRepository.findAll();
            eventTypeList.sort(new EventTypeComprator());
        } else {
            Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            List<EventType> eventTypeList = this.entityTypeRepository.findAll(sort);
        }

        return null;
    }

}
