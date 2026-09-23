package com.karainc.dailytalk.domain.member.repository;

import com.karainc.dailytalk.domain.member.entity.LoginMember;
import com.karainc.dailytalk.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface LoginMemberRepository extends JpaRepository<LoginMember, Long> {

    LoginMember findByLoginDate(LocalDate localDate);

    @Query("SELECT m FROM LoginMember m WHERE m.loginDate BETWEEN ?1 AND ?2")
    List<LoginMember> findWithinAWeek(LocalDate weekAgo, LocalDate today);

    @Query("SELECT m FROM LoginMember m WHERE YEAR(m.loginDate) = YEAR(:today) AND MONTH(m.loginDate) = MONTH(:today)")
    List<LoginMember> findByCurrentMonth(LocalDate today);
}
