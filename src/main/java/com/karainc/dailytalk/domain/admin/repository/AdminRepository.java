package com.karainc.dailytalk.domain.admin.repository;


import com.karainc.dailytalk.domain.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin ,Long> {

    Admin findByAdminKey (String adminCode);

    Admin findByAdminId (String adminId);


}
