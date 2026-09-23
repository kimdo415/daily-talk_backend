package com.karainc.dailytalk.domain.share.repository;

import com.karainc.dailytalk.domain.behavior.entity.BehavResult;
import com.karainc.dailytalk.domain.language.entity.LangResult;
import com.karainc.dailytalk.domain.share.entity.Share;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareRepository extends JpaRepository<Share, Long> {

    Share findByShareCode(String shareCode);

    Share findByLangResult(LangResult langResult);

    Share findByBehavResult(BehavResult behavResult);
}
