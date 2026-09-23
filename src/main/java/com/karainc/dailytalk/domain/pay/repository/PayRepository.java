package com.karainc.dailytalk.domain.pay.repository;

import com.karainc.dailytalk.domain.member.entity.Member;
import com.karainc.dailytalk.domain.pay.entity.Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PayRepository extends JpaRepository<Pay, Long> {

    Pay findByOrderId(String orderId);

    Pay findByOrPayIdx(Long payIdx);

    List<Pay> findByAuthenticatedAt(LocalDate today);

    @Query("SELECT m FROM Pay m WHERE m.authenticatedAt BETWEEN ?1 AND ?2")
    List<Pay> findWithinAWeek(LocalDate weekAgo, LocalDate today);

    @Query("SELECT m FROM Pay m WHERE YEAR(m.authenticatedAt) = YEAR(:today) AND MONTH(m.authenticatedAt) = MONTH(:today)")
    List<Pay> findByCurrentMonth(LocalDate today);

    @Query("SELECT e FROM Pay e WHERE e.authenticatedAt >= :oneYearAgoStartOfDay AND e.authenticatedAt < :nowEndOfDay")
    List<Pay> findOneYearData(LocalDate oneYearAgoStartOfDay, LocalDate nowEndOfDay);


    @Query("SELECT e FROM Pay e WHERE e.member = :member AND e.authenticatedAt >= :oneYearAgoStartOfDay AND e.authenticatedAt < :nowEndOfDay")
    List<Pay> findOneYearDataForMember(@Param("member") Member member, @Param("oneYearAgoStartOfDay") LocalDate oneYearAgoStartOfDay, @Param("nowEndOfDay") LocalDate nowEndOfDay);
}
