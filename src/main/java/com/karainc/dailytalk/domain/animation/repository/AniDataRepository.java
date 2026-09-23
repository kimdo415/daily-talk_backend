package com.karainc.dailytalk.domain.animation.repository;


import com.karainc.dailytalk.domain.animation.entity.AniData;
import com.karainc.dailytalk.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AniDataRepository extends JpaRepository<AniData ,Long> {
    List<AniData> findByMember(Member member);

    List<AniData> findByAniRef(Long aniIdx);
}
