package com.karainc.dailytalk.domain.quiz.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="quiz_id")
    private Long quizIdx;

    @Column(name="quiz_title")
    private String quizTitle;

    @Column(name="quiz_sub_title")
    private String quizSubTitle;

    @Column(name="quiz_level")
    private String quizLevel;

    @Column(name = "quiz_age")
    private String quizAge;

    @Column(name="quiz_category_ref")
    private Long categoryRef;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name="quiz_questions" ,joinColumns = @JoinColumn(name="quiz_id"))
    @Column(name="questions")
    private List<String> questions;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name="quiz_choices" ,joinColumns = @JoinColumn(name="quiz_id"))
    @Column(name="choices")
    private List<List<String>> choices;


    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name="quiz_answers" ,joinColumns = @JoinColumn(name="quiz_id"))
    @Column(name="answers")
    private List<String> answers;


    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name="quiz_images" ,joinColumns = @JoinColumn(name="quiz_id"))
    @Column(name="images")
    private List<String> images;

    @Column(name="title_image")
    private String titleImage;

    @Column(name="view")
    private Boolean view;
}
