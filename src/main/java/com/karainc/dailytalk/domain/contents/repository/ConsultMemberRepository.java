package com.karainc.dailytalk.domain.contents.repository;

import com.karainc.dailytalk.domain.contents.entity.ConsultMember;
import com.karainc.dailytalk.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultMemberRepository extends JpaRepository<ConsultMember,Long> {

    ConsultMember findByMember(Member member);

}
