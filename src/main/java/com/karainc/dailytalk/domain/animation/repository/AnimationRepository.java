package com.karainc.dailytalk.domain.animation.repository;

import com.karainc.dailytalk.domain.animation.entity.AniCategory;
import com.karainc.dailytalk.domain.animation.entity.Animation;
import com.karainc.dailytalk.domain.category.entitiy.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimationRepository extends JpaRepository<Animation, Long> {

    Animation findByAniIdx(long aniIdx);

    List<Animation> findByAniCategory(AniCategory aniCategory);
}
