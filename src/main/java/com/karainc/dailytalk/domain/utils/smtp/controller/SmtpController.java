package com.karainc.dailytalk.domain.utils.smtp.controller;


import com.karainc.dailytalk.domain.member.dto.request.FindEmailDto;
import com.karainc.dailytalk.domain.utils.coolsms.service.SmsService;
import com.karainc.dailytalk.domain.utils.smtp.controller.dto.ResultDto;
import com.karainc.dailytalk.domain.utils.smtp.controller.dto.request.MailDto;
import com.karainc.dailytalk.domain.utils.smtp.service.SmtpService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/smtp")
@RequiredArgsConstructor
public class SmtpController {

    private final SmtpService smtpService;
    @GetMapping
    public ResultDto sendMail(@RequestParam String email){
        return smtpService.sendEmail(email);
    }


//    @PostMapping("/temp-code")
//    public ResultDto sendTempCode(@RequestBody MailDto mailDto){
//
//    }


    @GetMapping("/test")
    public void testSend(){
        smtpService.sendTest();
    }
}
