package com.karainc.dailytalk.domain.notice.repository;

import com.karainc.dailytalk.domain.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findAll();

    Notice findByNoticeIdx(long noticeIdx);

    List<Notice> findByView(boolean view);
}
