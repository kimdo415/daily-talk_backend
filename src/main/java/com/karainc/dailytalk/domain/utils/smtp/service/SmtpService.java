package com.karainc.dailytalk.domain.utils.smtp.service;


import com.karainc.dailytalk.domain.exceptionhandler.DataNotMatchHandler;
import com.karainc.dailytalk.domain.member.dto.request.FindEmailDto;
import com.karainc.dailytalk.domain.member.entity.Member;
import com.karainc.dailytalk.domain.member.repository.MemberRepository;
import com.karainc.dailytalk.domain.utils.smtp.controller.dto.ResultDto;
import com.karainc.dailytalk.domain.utils.smtp.controller.dto.request.MailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.model.Balance;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Random;

@RequiredArgsConstructor
@Service
@Slf4j
public class SmtpService {

    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;


    private  final MemberRepository memberRepository;

    private static String generateRandomString(int length) {
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder randomString = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            randomString.append(randomChar);
        }

        return randomString.toString();
    }
    public ResultDto sendEmail(String to) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        ResultDto resultDto = new ResultDto();
        Member member=memberRepository.findByEmail(to);
        String tempkey=generateRandomString(7);
        member.setPassword(passwordEncoder.encode(tempkey));
        memberRepository.save(member);

        if(member==null){
            throw new DataNotMatchHandler("조회 불가능한 이메일 입니다");
        }
        try {
            // 이메일을 보내는 사람의 정보 설정
            helper.setFrom("dailytalkky@gmail.com");
            helper.setTo(to);
            helper.setSubject("임시비밀번호 안내");
            helper.setText(member.getNickName() +"님의 임시비밀번호:"+ tempkey);
            javaMailSender.send(message);

            log.info("메일 발송 받는사람" + member.getMemberId() + " " + member.getEmail());
            resultDto.setStatus("200");
            resultDto.setMessage("이메일 전송 완료");
            resultDto.setData("이메일 전송 완료");

        } catch (MessagingException e) {
            // 예외 처리
            e.printStackTrace();
        }
        return resultDto;

    }

    public void sendTest() {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        String s="rlaehdud12";

        try {
            // 이메일을 보내는 사람의 정보 설정
            helper.setFrom("dailytalkky@gmail.com");
            helper.setTo("kimdo415@naver.com");
            helper.setSubject("테스트임");
            helper.setText("테스트임");
            javaMailSender.send(message);

        } catch (MessagingException e) {
            // 예외 처리
            e.printStackTrace();
        }

    }

//    public ResultDto tempCode(MailDto mailDto){
//        Member member = memberRepository.findByMemberId(mailDto.getEmail());
//        if(member==null){
//            throw new DataNotMatchHandler("없는 이메일 입니다");
//        }
//        if(member.getName().equals(mailDto.getName())){
//            throw new DataNotMatchHandler("이메일 또는 이름이 다릅니다");
//        }
//        if(member.getLoginType().equals("01")){
//            throw new DataNotMatchHandler("카카오톡 회원은 조회 불가능합니다");
//        }
//
//
//    }



}
