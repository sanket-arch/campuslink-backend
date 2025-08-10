package com.api.campuslink.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Query")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Query {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String queryId;
    private String queryType;
    private String queryTitle;
    private String queryDescription;
    private String queryStatus;
    private String queryPriority;
    private String postedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate postedOn;
    @OneToMany(mappedBy = "query", cascade = CascadeType.ALL)
    private List<Answers> answers;
}
