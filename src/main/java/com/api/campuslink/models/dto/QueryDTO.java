package com.api.campuslink.models.dto;

import com.api.campuslink.utils.QueryTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class QueryDTO {
    private String queryId;
    private QueryTypeEnum queryType;
    private String queryTitle;
    private String queryDescription;
    private String queryStatus;
    private String queryPriority;
    private String postedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate postedOn;
}
