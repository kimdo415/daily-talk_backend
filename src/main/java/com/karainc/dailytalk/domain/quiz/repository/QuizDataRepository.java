package com.karainc.dailytalk.domain.quiz.repository;

import com.karainc.dailytalk.domain.quiz.entity.QuizData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizDataRepository extends JpaRepository<QuizData, Long> {

    List<QuizData> findByQuizCateGory(Long categoryIdx);

    List<QuizData> findBymRef(Long memberIdx);

    List<QuizData> findByQuizRef(Long quizIdx);


}
