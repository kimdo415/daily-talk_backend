package com.karainc.dailytalk.domain.site.repository;


import com.karainc.dailytalk.domain.site.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository extends JpaRepository<Site, Long> {
    Site findBySiteId(Long siteId);
}
