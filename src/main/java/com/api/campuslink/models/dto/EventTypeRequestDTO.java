package com.api.campuslink.models.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventTypeRequestDTO {
    @NotEmpty(message = "{messages.NotEmpty.EventTypeRequestDTO.eventCode}")
    private String eventCode;
    @NotEmpty(message = "{messages.NotEmpty.EventTypeRequestDTO.name}")
    private String name;
    private String description;
}
