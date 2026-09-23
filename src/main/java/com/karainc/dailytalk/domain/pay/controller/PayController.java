package com.karainc.dailytalk.domain.pay.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.karainc.dailytalk.domain.dashboard.controller.dto.requset.DashPayDto;
import com.karainc.dailytalk.domain.pay.controller.dto.request.PayDto;
import com.karainc.dailytalk.domain.pay.controller.dto.request.PayRequestDto;
import com.karainc.dailytalk.domain.pay.controller.dto.request.RefundDto;
import com.karainc.dailytalk.domain.pay.controller.dto.response.MyPayDetailDto;
import com.karainc.dailytalk.domain.pay.controller.dto.response.MyPayListDto;
import com.karainc.dailytalk.domain.pay.controller.dto.response.PayListDto;
import com.karainc.dailytalk.domain.pay.service.PayService;
import com.karainc.dailytalk.domain.utils.smtp.controller.dto.ResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/pay")
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    // test 결제라 실사용 api 아님
    @PostMapping("/test")
    public void testPay(@RequestBody PayDto payDto) {

        String key="dGVzdF9za19FUDU5THliWjhCcFh5amoxSm9YQlY2R1lvN3BSOg==";

        String getjsonBody = String.format("{\"authKey\":\"%s\",\"customerKey\":\"%s\"}", payDto.getAuthKey(),payDto.getCustomerKey());

        log.info("try 문 실행중");
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.tosspayments.com/v1/billing/authorizations/issue"))
                    .header("Authorization", "Basic dGVzdF9za19FUDU5THliWjhCcFh5amoxSm9YQlY2R1lvN3BSOg==")
                    .header("Content-Type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(getjsonBody))
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            log.info(response.body());

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = objectMapper.readValue(response.body(), new TypeReference<Map<String, Object>>() {});
            String billingKey="";
            Object billingKeyObject = jsonMap.get("billingKey");
            billingKey = billingKeyObject.toString();
            log.info(billingKey);

            //"{\"customerKey\":\"3QbEy2TzT6GJnyWXPiHPR\",\"amount\":4900,\"orderId\":\"gJR978yA6eMdAoKO7n4Hz\",\"orderName\":\"토스 프라임 구독\",\"customerEmail\":\"customer@email.com\",\"customerName\":\"박토스\",\"taxFreeAmount\":0}"

//            String getPayjsonBody = String.format("{\"customerKey\":\"%s\",\"amount\":4900,\"orderId\":\"gJR978yA6eMdAoKO7asdasdn4Hz\",\"orderName\":\"토스 프라임 구독\",\"customerEmail\":\"customer@email.com\",\"customerName\":\"박토스\",\"taxFreeAmount\":0}",payDto.getCustomerKey());
//            log.info("자동결제 시작");
//            HttpRequest payRequest = HttpRequest.newBuilder()
//                    .uri(URI.create("https://api.tosspayments.com/v1/billing/"+billingKey))
//                    .header("Authorization", "Basic dGVzdF9za19FUDU5THliWjhCcFh5amoxSm9YQlY2R1lvN3BSOg==")
//                    .header("Content-Type", "application/json")
//                    .method("POST", HttpRequest.BodyPublishers.ofString(getPayjsonBody))
//                    .build();
//            HttpResponse<String> payResponse = HttpClient.newHttpClient().send(payRequest, HttpResponse.BodyHandlers.ofString());
//            log.info(payResponse.body());
        } catch (IOException e) {
            // IOException 처리
            e.printStackTrace();
        } catch (InterruptedException e) {
            // InterruptedException 처리
            e.printStackTrace(); // 예외를 기록하거나 적절한 조치를 취할 수 있습니다.
        }
    }


    @PostMapping("/billing-key")
    public ResultDto getBillingKey(@RequestHeader(value = "X-MEMBER-TOKEN" ,required = false) String memberKey,
                                   @RequestBody PayDto payDto){
        return payService.billingKey(memberKey,payDto);
    }

    @PostMapping("/check")
    public ResultDto checking(@RequestHeader(value = "X-MEMBER-TOKEN",required = false) String memberKey,
                              @RequestBody PayRequestDto payRequestDto){
        return payService.payForBillingKey(memberKey,payRequestDto);
    }

    @GetMapping("/get-billing-key")
    public void getKey(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey){
        payService.checkB(memberKey);
    }

    @GetMapping("/force")
    public void force(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey,
                      @RequestParam Long contentsIdx){
        payService.ForceAttack(memberKey,contentsIdx);
    }


    @GetMapping("/member-list")
    public PayListDto getPayList(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey){
        return payService.getList(adminKey);
    }
    @PutMapping("/refund")
    public void re(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String memberKey,
                   @RequestBody RefundDto refundDto){
        payService.getRefund(memberKey,refundDto);
    }


    @GetMapping("/my-page")
    public MyPayListDto getMyPage(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey){
        return payService.getPayDashBoard(memberKey);
    }

    @GetMapping("/my-detail")
    public MyPayDetailDto getMyDetail(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey,
                                      @RequestParam Long payIdx){
        return payService.getMyDetail(memberKey,payIdx);
    }





}
