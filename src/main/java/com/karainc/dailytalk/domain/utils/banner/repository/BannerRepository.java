package com.karainc.dailytalk.domain.utils.banner.repository;


import com.karainc.dailytalk.domain.utils.banner.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    Banner findByBannerId(long bannerId);
}
