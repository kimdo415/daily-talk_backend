package com.karainc.dailytalk.domain.quiz.repository;


import com.karainc.dailytalk.domain.quiz.entity.Quiz;
import jakarta.persistence.Column;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Quiz findByQuizIdx(long quizId);

    List<Quiz> findAll();

    List<Quiz> findByView(Boolean view);

    List<Quiz> findByCategoryRef(Long ctRef);
}
