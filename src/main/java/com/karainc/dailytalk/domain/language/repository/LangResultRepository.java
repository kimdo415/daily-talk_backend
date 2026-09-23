package com.karainc.dailytalk.domain.language.repository;

import com.karainc.dailytalk.domain.language.entity.LangResult;
import com.karainc.dailytalk.domain.language.entity.Language;
import com.karainc.dailytalk.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LangResultRepository extends JpaRepository<LangResult,Long> {

    List<LangResult> findByMember(Member member);

    List<LangResult> findAll();

    LangResult findByLangResultId(Long id);
}
