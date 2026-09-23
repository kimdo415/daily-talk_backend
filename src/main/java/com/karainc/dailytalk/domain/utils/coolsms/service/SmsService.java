package com.karainc.dailytalk.domain.utils.coolsms.service;
import com.karainc.dailytalk.domain.exceptionhandler.DataNotMatchHandler;
import com.karainc.dailytalk.domain.utils.coolsms.Entitiy.Sms;
import com.karainc.dailytalk.domain.utils.coolsms.controller.dto.Response.CoolSmsDto;
import com.karainc.dailytalk.domain.utils.coolsms.repository.SmsRepository;
import com.karainc.dailytalk.domain.utils.coolsms.controller.dto.Request.ValidDto;
import com.karainc.dailytalk.domain.utils.coolsms.controller.dto.Response.ResultSmsDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import net.nurigo.sdk.message.model.Balance;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.Random;
import java.lang.reflect.Field;

@RequiredArgsConstructor
@Service
@Component
public class SmsService {

    private final SmsRepository smsRepository;
    private String apiKey="NCSFWQ97LWPZPI27";

    private String apiSecretKey="COPNSNNBJ61AOXXLUEERL4K9KA4SWJ9O";

    private DefaultMessageService messageService;

    @PostConstruct
    private void init(){
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
    }

    public CoolSmsDto sendOne(String phoneNumber) {

        Balance balance = this.messageService.getBalance();
        System.out.println(balance.getBalance());
        if(balance.getBalance()<=1){
            throw new DataNotMatchHandler("잔액부족");
        }

        Random rand  = new Random();
        String numStr = "";
        for(int i=0; i<4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr+=ran;
        }

        // 중복검사를 통해 먼저 이메일을 체크
        // 해당 로직을 통해 임시 코드와 전화번호 저장
        // 만약 인증번호를 찰나의 순간에 잊어버려 다시 해야하는 경우
        // 이미 db 상에 저장되어 있기 때문에 수정되어야함
        Sms sms=smsRepository.findByCheckPn(phoneNumber);
        if(!(sms==null)){
            sms.setCode(numStr);
            smsRepository.save(sms);
        } else {
            Sms tmpsms= Sms.builder()
                    .checkPn(phoneNumber)
                    .code(numStr)
                    .build();
            smsRepository.save(tmpsms);
        }

        Message message = new Message();
            // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom("01088832251");
        message.setTo(phoneNumber);
        message.setText("휴대폰인증 메시지 인증번호는" + "["+numStr+"]" + "입니다.");

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

        CoolSmsDto coolSmsDto = new CoolSmsDto();
        coolSmsDto.setStatus("200");
        coolSmsDto.setMessage("문자 발송 완료 인증번호를 확인해주세요");
        coolSmsDto.setData("문자 발송 완료 인증번호를 확인해주세요");
        return coolSmsDto;

    }



    // 인증시 해당 번호는 삭제됨
    public ResultSmsDto Validation(String pN, String code){
        Sms sms = smsRepository.findByCheckPn(pN);
        if(sms==null){
            throw new DataNotMatchHandler("인증 절차 오류 먼저 인증번호를 발급 받아주세요");
        }else if (!(sms.getCheckPn().equals(pN)&& sms.getCode().equals(code))){
            throw new DataNotMatchHandler("인증 번호가 일치하지 않습니다");
        }
        ResultSmsDto resultSmsDto =new ResultSmsDto();
        resultSmsDto.setStatus("200");
        resultSmsDto.setMessage("인증 성공 가입을 계속 진행해주세요");
        resultSmsDto.setData("인증 성공 가입을 계속 진행해주세요");
        smsRepository.delete(sms);
        return resultSmsDto;
    }



}
