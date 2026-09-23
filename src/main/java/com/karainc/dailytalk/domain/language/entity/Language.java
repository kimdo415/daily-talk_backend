package com.karainc.dailytalk.domain.language.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="language_id")
    private Long languageId;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name="lang_question" ,joinColumns = @JoinColumn(name="language_id"))
    @Column(name = "languages")
    private List<String> languages;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "language_type",joinColumns =@JoinColumn(name ="language_id" ))
    @Column(name ="language_enum")
    private List<String> languageEnum;

    @Column(name = "during_date")
    private String duringDate;

    @Column(name = "limit_date")
    private String limitDate;

}
