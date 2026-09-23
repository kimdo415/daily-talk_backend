package com.karainc.dailytalk.domain.animation.repository;

import com.karainc.dailytalk.domain.animation.entity.AniCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AniCategoryRepository extends JpaRepository<AniCategory, Long> {

    List<AniCategory> findAll();

    AniCategory findByAniCategoryIdx(long anictIdx);
}
