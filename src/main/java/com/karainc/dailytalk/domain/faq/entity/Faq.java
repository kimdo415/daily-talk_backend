package com.karainc.dailytalk.domain.faq.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Faq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long faqId;

    @Column
    private String faqTitle;

    @Column(columnDefinition = "TEXT")
    private String faqContent;

    @Column
    private String writer;

    @Column
    private String date;

    @Column
    private Boolean view;
}
