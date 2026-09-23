package com.karainc.dailytalk.domain.behavior.repository;

import com.karainc.dailytalk.domain.behavior.entity.Behavior;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BehaviorRepository extends JpaRepository<Behavior ,Long> {
    Behavior findByBehaviorId(long id);
}
