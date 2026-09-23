package com.karainc.dailytalk.domain.contents.repository;


import com.karainc.dailytalk.domain.contents.entity.CheckConMember;
import com.karainc.dailytalk.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckConMemberRepository extends JpaRepository<CheckConMember ,Long> {
    CheckConMember findByMember(Member member);
}
