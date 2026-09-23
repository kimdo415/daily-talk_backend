package com.karainc.dailytalk.domain.visit.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="visitIdx")
    private Long visitIdx;

    @Column(name ="visit_day")
    private LocalDate visitDay;

    @Column(name ="visit_Count")
    private Integer visitCount;
}
