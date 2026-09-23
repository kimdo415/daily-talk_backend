package com.karainc.dailytalk.domain.contents.repository;

import com.karainc.dailytalk.domain.contents.entity.Contents;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentsRepository extends JpaRepository<Contents, Long> {

    List<Contents> findAll();

    Contents findByContentsIdx(long contentsId);

    List<Contents> findByViewStatus(boolean viewStatus);
}
