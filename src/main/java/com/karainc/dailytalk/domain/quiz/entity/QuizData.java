package com.karainc.dailytalk.domain.quiz.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuizData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="quiz_data_id")
    private Long quizDataIdx;

    @Column(name ="ref_quiz_idx")
    private Long quizRef;

    @Column(name ="ref_member_idx")
    private Long mRef;

    @Column(name="quiz_type")
    private Long quizCateGory;

    @Column(name = "quiz_test_day")
    private String testDay;

    @Column(name = "quiz_point")
    private int quizPoint;

    @Column(name = "correct_rate")
    private String correctRate;

    @Column(name = "incorrect_rate")
    private String incorrectRate;

    @Column(name = "test_time")
    private Integer testTime;
}
