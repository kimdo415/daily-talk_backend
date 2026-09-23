package com.karainc.dailytalk.domain.visit.repository;

import com.karainc.dailytalk.domain.member.entity.LoginMember;
import com.karainc.dailytalk.domain.visit.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface VisitRepository extends JpaRepository<Visit ,Long> {

    Visit findByVisitDay (LocalDate today);

    @Query("SELECT m FROM Visit m WHERE m.visitDay BETWEEN ?1 AND ?2")
    List<Visit> findWithinAWeek(LocalDate weekAgo, LocalDate today);

    @Query("SELECT m FROM Visit m WHERE YEAR(m.visitDay) = YEAR(:today) AND MONTH(m.visitDay) = MONTH(:today)")
    List<Visit> findByCurrentMonth(LocalDate today);
}
