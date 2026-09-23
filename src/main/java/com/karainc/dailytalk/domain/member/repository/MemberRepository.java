package com.karainc.dailytalk.domain.member.repository;

import com.karainc.dailytalk.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {


    List<Member> findAll();

    Member findByMemberId(String memberId);
    Member findByIdx(Long idx);

    Member findByMemberKey(String memberKey);

    Member findByName(String name);
    Member findByEmail(String email);

    List<Member> findByMemberType(String type);

    Member findByPhoneNumber(String phoneNumber);
    Member findByNickName(String nickName);

    Member findByCustomerKey(String customerKey);

    List<Member> findByCreateDay(LocalDate createDay);

    @Query("SELECT m FROM Member m WHERE m.createDay BETWEEN ?1 AND ?2")
    List<Member> findWithinAWeek(LocalDate weekAgo, LocalDate today);

    @Query("SELECT m FROM Member m WHERE YEAR(m.createDay) = YEAR(:today) AND MONTH(m.createDay) = MONTH(:today)")
    List<Member> findByCurrentMonth(LocalDate today);



//    boolean existsByMemberKey(String key);
//    boolean existsByMemberId(String memberId);
//    boolean existsByPhoneNumber(String phoneNumber);
//
//    boolean existsByNickName(String nickName);
//    boolean existsByEmail(String email);

}
