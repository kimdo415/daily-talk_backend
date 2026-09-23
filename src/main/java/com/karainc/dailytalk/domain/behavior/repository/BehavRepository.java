package com.karainc.dailytalk.domain.behavior.repository;

import com.karainc.dailytalk.domain.behavior.entity.BehavResult;
import com.karainc.dailytalk.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BehavRepository extends JpaRepository<BehavResult , Long> {

    List<BehavResult> findAll();

    List<BehavResult> findByMember(Member member);

    BehavResult findByBehavResultId(Long id);

}
