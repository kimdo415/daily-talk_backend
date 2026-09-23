package com.karainc.dailytalk.domain.utils.coolsms.controller;



import com.karainc.dailytalk.domain.utils.coolsms.controller.dto.Request.SmsPnDto;
import com.karainc.dailytalk.domain.utils.coolsms.controller.dto.Request.ValidDto;
import com.karainc.dailytalk.domain.utils.coolsms.controller.dto.Response.CoolSmsDto;
import com.karainc.dailytalk.domain.utils.coolsms.controller.dto.Response.ResultSmsDto;
import com.karainc.dailytalk.domain.utils.coolsms.service.SmsService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.model.Balance;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/cool-sms")
@RequiredArgsConstructor
public class SmsController {


    private final SmsService smsService;
    @GetMapping
    public CoolSmsDto check(@RequestParam String pN){
        return smsService.sendOne(pN);
    }

    @GetMapping("/vaild")
    public ResultSmsDto validation(@RequestParam String pN,
                                   @RequestParam String code){
        return smsService.Validation(pN,code);
    }

}