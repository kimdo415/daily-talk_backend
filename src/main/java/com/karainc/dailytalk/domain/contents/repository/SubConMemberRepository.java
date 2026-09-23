package com.karainc.dailytalk.domain.contents.repository;

import com.karainc.dailytalk.domain.contents.entity.SubConMember;
import com.karainc.dailytalk.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubConMemberRepository extends JpaRepository<SubConMember, Long> {

    SubConMember findByMember(Member member);

    List<SubConMember> findAll();
}
