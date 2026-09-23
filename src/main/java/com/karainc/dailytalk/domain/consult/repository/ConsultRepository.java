package com.karainc.dailytalk.domain.consult.repository;

import com.karainc.dailytalk.domain.consult.entity.Consult;
import com.karainc.dailytalk.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultRepository extends JpaRepository<Consult,Long> {

    List<Consult> findAll();

    List<Consult> findByMember(Member member);

    Consult findByConsultIdx(long consultIdx);
}
