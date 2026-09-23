package com.karainc.dailytalk.domain.language.repository;

import com.karainc.dailytalk.domain.language.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LanguageRepository extends JpaRepository<Language ,Long> {
    Language findByLanguageId(long langId);

    List<Language> findAll();
}
