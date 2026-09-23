package com.karainc.dailytalk.domain.behavior.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Behavior {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="behavior_id")
    private Long behaviorId;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "behavior_question",joinColumns =@JoinColumn(name ="behavior_id" ))
    @Column(name ="behaviors")
    private List<String> behaviors;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "behavior_type",joinColumns =@JoinColumn(name ="behavior_id" ))
    @Column(name ="behavior_enum")
    private List<String> behaviorEnum;


    @Column(name="during date")
    private String duringDate;

    @Column(name="limit_date")
    private String limitDate;

}
