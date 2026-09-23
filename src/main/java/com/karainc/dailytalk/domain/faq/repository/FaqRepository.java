package com.karainc.dailytalk.domain.faq.repository;

import com.karainc.dailytalk.domain.faq.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaqRepository extends JpaRepository<Faq ,Long>{

    List<Faq> findAll();

    List<Faq> findByView(Boolean status);
    Faq findByFaqId(long faqIdx);
}
