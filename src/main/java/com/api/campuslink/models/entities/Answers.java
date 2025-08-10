package com.api.campuslink.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Reference;

@Entity
@Table(name = "answers")
@Data
@NoArgsConstructor
public class Answers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String answerId;

    @ManyToOne
    @JoinColumn(name = "query_id", referencedColumnName = "id", nullable = false)
    private Query query;
    private String answerText;
    private String answeredBy;
    private String answeredOn;
}
