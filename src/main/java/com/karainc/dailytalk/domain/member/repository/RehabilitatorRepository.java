package com.karainc.dailytalk.domain.member.repository;

import com.karainc.dailytalk.domain.member.entity.Rehabilitator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RehabilitatorRepository extends JpaRepository<Rehabilitator, Long> {

    Rehabilitator findByRehabilIdx(Long rehabilIdx);

    Rehabilitator findByRefId(Long refId);

    long countByStatus(boolean status);

    List<Rehabilitator> findByStatus(boolean status);

    List<Rehabilitator> findAll();
}
