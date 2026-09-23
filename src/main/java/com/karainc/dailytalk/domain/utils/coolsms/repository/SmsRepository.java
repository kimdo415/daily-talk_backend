package com.karainc.dailytalk.domain.utils.coolsms.repository;


import com.karainc.dailytalk.domain.utils.coolsms.Entitiy.Sms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsRepository extends JpaRepository<Sms, Long> {
    Sms findByCheckPn (String pn);
}
