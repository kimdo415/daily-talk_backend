package com.karainc.dailytalk.domain.pay.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.karainc.dailytalk.domain.admin.service.AdminService;
import com.karainc.dailytalk.domain.contents.entity.CheckConMember;
import com.karainc.dailytalk.domain.contents.entity.ConsultMember;
import com.karainc.dailytalk.domain.contents.entity.Contents;
import com.karainc.dailytalk.domain.contents.entity.SubConMember;
import com.karainc.dailytalk.domain.contents.repository.CheckConMemberRepository;
import com.karainc.dailytalk.domain.contents.repository.ConsultMemberRepository;
import com.karainc.dailytalk.domain.contents.repository.ContentsRepository;
import com.karainc.dailytalk.domain.contents.repository.SubConMemberRepository;
import com.karainc.dailytalk.domain.exceptionhandler.DataNotMatchHandler;
import com.karainc.dailytalk.domain.exceptionhandler.NoDataExceptionHandler;
import com.karainc.dailytalk.domain.member.entity.Member;
import com.karainc.dailytalk.domain.member.repository.MemberRepository;
import com.karainc.dailytalk.domain.member.service.MemberService;
import com.karainc.dailytalk.domain.pay.controller.dto.data.MyPayDetail;
import com.karainc.dailytalk.domain.pay.controller.dto.data.MyPayList;
import com.karainc.dailytalk.domain.pay.controller.dto.data.PayList;
import com.karainc.dailytalk.domain.pay.controller.dto.request.PayDto;
import com.karainc.dailytalk.domain.pay.controller.dto.request.PayRequestDto;
import com.karainc.dailytalk.domain.pay.controller.dto.request.RefundDto;
import com.karainc.dailytalk.domain.pay.controller.dto.response.MyPayDetailDto;
import com.karainc.dailytalk.domain.pay.controller.dto.response.MyPayListDto;
import com.karainc.dailytalk.domain.pay.controller.dto.response.PayListDto;
import com.karainc.dailytalk.domain.pay.controller.dto.response.RefundMsgDto;
import com.karainc.dailytalk.domain.pay.entity.Pay;
import com.karainc.dailytalk.domain.pay.entity.PayMember;
import com.karainc.dailytalk.domain.pay.repository.PayMemberRepository;
import com.karainc.dailytalk.domain.pay.repository.PayRepository;
import com.karainc.dailytalk.domain.utils.smtp.controller.dto.ResultDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequiredArgsConstructor
@EnableScheduling
@Service
@Slf4j
public class PayService {

    private final PayRepository payRepository;
    private final PayMemberRepository payMemberRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final ContentsRepository contentsRepository;
    private final SubConMemberRepository subConMemberRepository;
    private final CheckConMemberRepository checkConMemberRepository;
    private final ConsultMemberRepository consultMemberRepository;
    private final AdminService adminService;

    public ResultDto billingKey(String memberKey, PayDto payDto){
        log.info("회원 토큰 검증 : 빌링키 발급");
        memberService.checkMember(memberKey);

        // api key=> 토스페이먼츠 실심사후 발급 후 교체요망(현제 테스트 api)
        //
        String key="bGl2ZV9za195WnFta0tlUDhnUHZCTFJMUVhSZDNiUVJ4QjlsOg==";

        String getjsonBody = String.format("{\"authKey\":\"%s\",\"customerKey\":\"%s\"}", payDto.getAuthKey(),payDto.getCustomerKey());

        ResultDto resultDto = new ResultDto();
        log.info("try 문 실행중");
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.tosspayments.com/v1/billing/authorizations/issue"))
                    .header("Authorization", "Basic bGl2ZV9za195WnFta0tlUDhnUHZCTFJMUVhSZDNiUVJ4QjlsOg==")
                    .header("Content-Type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(getjsonBody))
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            log.info(response.body());

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = objectMapper.readValue(response.body(), new TypeReference<Map<String, Object>>() {});
            String billingKey;
            Object billingKeyObject = jsonMap.get("billingKey");
            if(billingKeyObject==null){
                throw new NoDataExceptionHandler("빌링키 발급 실패");
            }
            else {
                billingKey = billingKeyObject.toString();
                log.info("결제키 발급: "+billingKey);
            }

            Member member = memberRepository.findByMemberKey(memberKey);
            PayMember payMember = payMemberRepository.findByMember(member);

            if(payMember != null){
                log.info("재구독 회원 빌링키 재발급");
                payMember.setBillingKey(billingKey);
                payMemberRepository.save(payMember);
            }else{
                log.info("신규 구독회원 빌링키 발급");
                PayMember newPaymember= PayMember.builder()
                        .member(member)
                        .billingKey(billingKey)
                        .repay(true)
                        .build();
                payMemberRepository.save(newPaymember);
            }

            resultDto.setStatus("200");
            resultDto.setMessage("빌링키 발급");
            resultDto.setData("빌링키 발급 성공 결제 진행");

        } catch (IOException e) {
            // IOException 처리
            e.printStackTrace();
            resultDto.setStatus("200");
            resultDto.setMessage("빌링키 발급실패");
            resultDto.setData("빌링키 발급 실패");
        } catch (InterruptedException e) {
            // InterruptedException 처리
            e.printStackTrace(); // 예외를 기록하거나 적절한 조치를 취할 수 있습니다.
        }
        return resultDto;
    }

    public ResultDto payForBillingKey(String memberKey, PayRequestDto payRequestDto){
        memberService.checkMember(memberKey);
        Member member = memberRepository.findByMemberKey(memberKey);
        Contents contents = contentsRepository.findByContentsIdx(payRequestDto.getContentsIdx());

        ResultDto resultDto= new ResultDto();

        if(contents==null){
            throw new NoDataExceptionHandler("없어진 컨텐츠 입니다");
        }else{
            if(contents.getViewStatus()==Boolean.FALSE){
                throw new DataNotMatchHandler("판매 불가한 컨텐츠 입니다");
            }
        }

        PayMember payMember = payMemberRepository.findByMember(member);

        if(payMember==null){
            throw new NoDataExceptionHandler("빌링키 조회 실패 카드 등록부터 해주세요");
        }

        String getOrderId=getOrderId();
        String getPayjsonBody = String.format("{\"customerKey\":\"%s\",\"amount\":%d,\"orderId\":\"%s\",\"orderName\":\"%s\",\"customerEmail\":\"%s\",\"customerName\":\"%s\",\"taxFreeAmount\":0}",
                member.getCustomerKey(), contents.getPrice(), getOrderId, contents.getContentsName(), member.getEmail(), member.getName());

        try {
                HttpRequest payRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.tosspayments.com/v1/billing/"+payMember.getBillingKey()))
                    .header("Authorization", "Basic bGl2ZV9za195WnFta0tlUDhnUHZCTFJMUVhSZDNiUVJ4QjlsOg==")
                    .header("Content-Type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(getPayjsonBody))
                    .build();
                HttpResponse<String> payResponse = HttpClient.newHttpClient().send(payRequest, HttpResponse.BodyHandlers.ofString());
                log.info(payResponse.body());

                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> jsonMap = objectMapper.readValue(payResponse.body(), new TypeReference<Map<String, Object>>() {});
                String status;
                String payKey;
                Object getStatus = jsonMap.get("status");
                if(getStatus==null){
                    throw new NoDataExceptionHandler("결제 실패");
                }else{
                    status=getStatus.toString();
                    Object getKey = jsonMap.get("paymentKey");
                    payKey = getKey.toString();
                    log.info("상태확인: " +status);
                }
                if(!status.equals("DONE")){
                    throw new NoDataExceptionHandler("결제 실패 : 카드정보 오류");
                }
                log.info("결제 성공");
                member.setPayStatus(true);
                payMember.setRepay(true);

                System.out.println("가격 파싱 : " + contents.getPrice());

                Pay pay = Pay.builder()
                            .member(member)
                            .contentCode(contents.getContentsIdx())
                            .amount(contents.getPrice())
                            .authenticatedAt(LocalDate.now())
                            .orderId(getOrderId)
                            .payContents(contents)
                            .paymentKey(payKey)
                            .refundStatus(Boolean.FALSE)
                            .build();

                if(contents.getContentType().equals("01")){
                    log.info("구독형 컨텐츠 결제");
                    SubConMember subConMember =subConMemberRepository.findByMember(member);
                    if(subConMember==null){
                        log.info("신규 결제");
                        SubConMember newsubConMember = SubConMember.builder()
                                .member(member)
                                .endDate(LocalDate.now().plusDays(Integer.parseInt(contents.getDuringDate())).toString())
                                .contents(contents)
                                .build();
                        subConMemberRepository.save(newsubConMember);
                    }else{
                        log.info("기존 회원 결제");
                        subConMember.setEndDate(LocalDate.now().plusDays(Integer.parseInt(contents.getDuringDate())).toString());
                        subConMember.setContents(contents);
                        subConMemberRepository.save(subConMember);
                    }

                }else if(contents.getContentType().equals("02")){
                    log.info("진단 검사형 컨텐츠 결제");
                    CheckConMember checkConMember = checkConMemberRepository.findByMember(member);
                    if(checkConMember==null){
                        log.info("신규 결제");
                        CheckConMember newcheckConMember = CheckConMember.builder()
                                .contentType(contents.getContentType())
                                .member(member)
                                .useCount(contents.getUseCount())
                                .contents(contents)
                                .build();
                        checkConMemberRepository.save(newcheckConMember);
                    }else{
                        log.info("기존 회원 결제");
                        checkConMember.setUseCount(checkConMember.getUseCount()+contents.getUseCount());
                        checkConMember.setContents(contents);
                        checkConMemberRepository.save(checkConMember);
                    }
                }else if(contents.getContentType().equals("03")){
                    log.info("상담형 컨텐츠 결제");
                    ConsultMember consultMember = consultMemberRepository.findByMember(member);
                    if(consultMember==null){
                        log.info("신규 결제");
                        ConsultMember newconsultMember = ConsultMember.builder()
                                .contentType(contents.getContentType())
                                .member(member)
                                .useCount(contents.getUseCount())
                                .contents(contents)
                                .build();
                        consultMemberRepository.save(newconsultMember);
                    }else {
                        log.info("기존 회원 결제");
                        consultMember.setUseCount(consultMember.getUseCount()+contents.getUseCount());
                        consultMember.setContents(contents);
                        consultMemberRepository.save(consultMember);
                    }
                }

                payRepository.save(pay);
                payMemberRepository.save(payMember);
                memberRepository.save(member);

                contents.setSailCount(contents.getSailCount()+1);
                contentsRepository.save(contents);

                resultDto.setStatus("200");
                resultDto.setData("결제 성공");
                resultDto.setMessage("결제 성공");
                return resultDto;

        }catch (IOException e) {
            // IOException 처리
            e.printStackTrace();
        } catch (InterruptedException e) {
            // InterruptedException 처리
            e.printStackTrace(); // 예외를 기록하거나 적절한 조치를 취할 수 있습니다.
        }
        return resultDto;
    }


    public String getOrderId(){
        String orderId;
        while(true){
            UUID uuid=UUID.randomUUID();
            orderId = uuid.toString();
            Pay pay = payRepository.findByOrderId(orderId);
            if(pay==null){
                break;
            }
        }
        return orderId;
    }

    public void checkB(String memberKey){
        memberService.checkMember(memberKey);

        Member member = memberRepository.findByMemberKey(memberKey);

        PayMember payMember = payMemberRepository.findByMember(member);
        if(payMember==null){
            throw new NoDataExceptionHandler("카드 등록을 진행해주세요");
        }else if(payMember.getBillingKey()==null || payMember.getBillingKey().isEmpty()){
            throw new NoDataExceptionHandler("빌링키 오류 카드등록부터 진행해주세요");
        }
    }


    public void ForceAttack(String memberKey,Long contentsIdx){

        memberService.checkMember(memberKey);
        Contents contents = contentsRepository.findByContentsIdx(contentsIdx);
        if(contents==null){
            throw new NoDataExceptionHandler("없다 임마");
        }

        Member member = memberRepository.findByMemberKey(memberKey);

        if(contents.getContentType().equals("01")){
            log.info("구독형 컨텐츠 결제");
            SubConMember subConMember =subConMemberRepository.findByMember(member);
            if(subConMember==null){
                log.info("신규 결제");
                SubConMember newsubConMember = SubConMember.builder()
                        .member(member)
                        .endDate("2029-12-10")
                        .contents(contents)
                        .build();
                subConMemberRepository.save(newsubConMember);
            }else{
                log.info("기존 회원 결제");
                subConMember.setEndDate(LocalDate.now().plusDays(Integer.parseInt(contents.getDuringDate())).toString());
                subConMember.setContents(contents);
                subConMemberRepository.save(subConMember);
            }

        }else if(contents.getContentType().equals("02")){
            log.info("진단 검사형 컨텐츠 결제");
            CheckConMember checkConMember = checkConMemberRepository.findByMember(member);
            if(checkConMember==null){
                log.info("신규 결제");
                CheckConMember newcheckConMember = CheckConMember.builder()
                        .contentType(contents.getContentType())
                        .member(member)
                        .useCount(10000)
                        .contents(contents)
                        .build();
                checkConMemberRepository.save(newcheckConMember);
            }else{
                log.info("기존 회원 결제");
                checkConMember.setUseCount(checkConMember.getUseCount()+contents.getUseCount());
                checkConMember.setContents(contents);
                checkConMemberRepository.save(checkConMember);
            }
        }else if(contents.getContentType().equals("03")){
            log.info("상담형 컨텐츠 결제");
            ConsultMember consultMember = consultMemberRepository.findByMember(member);
            if(consultMember==null){
                log.info("신규 결제");
                ConsultMember newconsultMember = ConsultMember.builder()
                        .contentType(contents.getContentType())
                        .member(member)
                        .useCount(10000)
                        .contents(contents)
                        .build();
                consultMemberRepository.save(newconsultMember);
            }else {
                log.info("기존 회원 결제");
                consultMember.setUseCount(consultMember.getUseCount()+contents.getUseCount());
                consultMember.setContents(contents);
                consultMemberRepository.save(consultMember);
            }
        }
    }


    @Scheduled(cron = "0 0 10 * * ?")
    public void scheduledTask() {
        // 5초마다 실행되는 스케줄링 로직을 작성합니다.
        List<SubConMember> subConMembers = subConMemberRepository.findAll();
        int i=subConMembers.size();
        if(i==0){
            return;
        }

        LocalDate localDate = LocalDate.now();

        DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for(SubConMember subConMember : subConMembers){
            LocalDate getMemberSEnd = LocalDate.parse(subConMember.getEndDate(),formatter);
            if(!localDate.isAfter(getMemberSEnd)){
                log.info("구독기간 남음");
                return;
            }
            Long c;
            if(subConMember.getContents()==null){
                return;
            }

            c=subConMember.getContents().getContentsIdx();
            Contents contents = contentsRepository.findByContentsIdx(c);

            if(contents.getViewStatus()==Boolean.FALSE){
                return;
            }

            long k = subConMember.getMember().getIdx();
            Member member = memberRepository.findByIdx(k);
            if(member!=null){
                if(member.getDState()==Boolean.FALSE){
                    return;
                }else{
                    String name = member.getName();
                    String ckey =member.getCustomerKey();
                    String mail = member.getEmail();
                    PayMember payMember = payMemberRepository.findByMember(member);
                    if (payMember != null) {
                        scheduledPay(name,ckey,mail,contents,payMember.getBillingKey(),k);
                        subConMember.setEndDate(LocalDate.now().plusDays(Integer.parseInt(contents.getDuringDate())).toString());
                        subConMemberRepository.save(subConMember);
                    }
                }
            }
        }
    }

    public void scheduledPay(String name,String customerKey,String mail,Contents contents,String billingKey,Long k){
        String getOrderId=getOrderId();
        log.info("정기 결제");
        String getPayjsonBody = String.format("{\"customerKey\":\"%s\",\"amount\":%d,\"orderId\":\"%s\",\"orderName\":\"%s\",\"customerEmail\":\"%s\",\"customerName\":\"%s\",\"taxFreeAmount\":0}",
                customerKey, contents.getPrice(), getOrderId,contents.getContentsName(), mail, name);

        if(contents==null || contents.getViewStatus()==Boolean.FALSE) {
            return;
        }

        try {
            HttpRequest payRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.tosspayments.com/v1/billing/"+billingKey))
                    .header("Authorization", "Basic bGl2ZV9za195WnFta0tlUDhnUHZCTFJMUVhSZDNiUVJ4QjlsOg==")
                    .header("Content-Type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(getPayjsonBody))
                    .build();
            HttpResponse<String> payResponse = HttpClient.newHttpClient().send(payRequest, HttpResponse.BodyHandlers.ofString());
            log.info(payResponse.body());

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = objectMapper.readValue(payResponse.body(), new TypeReference<Map<String, Object>>() {});
            String status;
            String key;
            Object getStatus = jsonMap.get("status");
            if(getStatus==null){
                throw new NoDataExceptionHandler("결제 실패");
            }else{
                status=getStatus.toString();
                Object getKey=jsonMap.get("paymentKey");
                log.info("상태확인: " +status);
                key = getKey.toString();
            }
            if(!status.equals("DONE")){
                throw new NoDataExceptionHandler("결제 실패 : 카드정보 오류");
            }
            log.info("결제 성공");

            Member member = memberRepository.findByIdx(k);
            Pay pay = Pay.builder()
                    .member(member)
                    .contentCode(contents.getContentsIdx())
                    .amount(contents.getPrice())
                    .authenticatedAt(LocalDate.now())
                    .orderId(getOrderId)
                    .payContents(contents)
                    .paymentKey(key)
                    .refundStatus(Boolean.FALSE)
                    .build();

            contents.setSailCount(contents.getSailCount());
            payRepository.save(pay);
            contentsRepository.save(contents);

//            if(contents.getContentType().equals("01")){
//                log.info("구독형 컨텐츠 결제");
//                SubConMember subConMember =subConMemberRepository.findByMember(member);
//                if(subConMember==null){
//                    log.info("신규 결제");
//                    SubConMember newsubConMember = SubConMember.builder()
//                            .member(member)
//                            .endDate(LocalDate.now().plusDays(Integer.parseInt(contents.getDuringDate())).toString())
//                            .build();
//                    subConMemberRepository.save(newsubConMember);
//                }else{
//                    log.info("기존 회원 결제");
//                    subConMember.setEndDate(LocalDate.now().plusDays(Integer.parseInt(contents.getDuringDate())).toString());
//                    subConMemberRepository.save(subConMember);
//                }

//            }

        }catch (IOException e) {
            // IOException 처리
        } catch (InterruptedException e) {
            // InterruptedException 처리
        }

    }

    public void getRefund(String adminKey, RefundDto refundDto){

//        RefundMsgDto refundMsgDto = new RefundMsgDto();
        adminService.checkAdmin(adminKey);

        Pay refundPay= payRepository.findByOrPayIdx(refundDto.getPayIdx());
        if(refundPay==null){
            throw new NoDataExceptionHandler("없는 결제 내역 입니다");
        }

        if(refundPay.getAmount()<refundDto.getAmount()){
            throw new DataNotMatchHandler("결제 금액보다 더 많은금액을 환불 할 수 없습니다");
        }else if(refundPay.getRefundStatus()==Boolean.TRUE){
            throw new DataNotMatchHandler("이미 환불된 건입니다");
        }

        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.tosspayments.com/v1/payments/"+refundPay.getPaymentKey()+"/cancel"))
                    .header("Authorization", "Basic bGl2ZV9za195WnFta0tlUDhnUHZCTFJMUVhSZDNiUVJ4QjlsOg==")
                    .header("Content-Type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString("{\"cancelReason\":\"고객이 취소를 원함\",\"cancelAmount\":" + refundDto.getAmount()+ "}"))
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = objectMapper.readValue(response.body(), new TypeReference<Map<String, Object>>() {});
            log.info(jsonMap.toString());
            Object status = jsonMap.get("status");
            if(status==null){
                throw new NoDataExceptionHandler("취소 실패");
            }

            refundPay.setRefundStatus(Boolean.TRUE);
            refundPay.setRefundAmount(refundDto.getAmount());
            refundPay.setRefundDate(LocalDate.now());
            payRepository.save(refundPay);

            if(refundPay.getPayContents().getContentType().equals("01")){
                log.info("구독형 컨텐츠 환불");
                SubConMember subConMember = subConMemberRepository.findByMember(refundPay.getMember());
                subConMemberRepository.delete(subConMember);
            }else if(refundPay.getPayContents().getContentType().equals("02")){
                log.info("상담형 컨텐츠 환불");
                ConsultMember consultMember = consultMemberRepository.findByMember(refundPay.getMember());
                consultMemberRepository.delete(consultMember);
            }

            if(refundPay.getPayContents().getContentType().equals("03")){
                log.info("진단 검사형 컨텐츠 환불");
                CheckConMember checkConMember = checkConMemberRepository.findByMember(refundPay.getMember());
                checkConMemberRepository.delete(checkConMember);
            }





        }catch (IOException e) {
            // IOException 처리
            e.printStackTrace();
        } catch (InterruptedException e) {
            // InterruptedException 처리
            e.printStackTrace(); // 예외를 기록하거나 적절한 조치를 취할 수 있습니다.
        }
    }

    public PayListDto getList(String adminKey){
        adminService.checkAdmin(adminKey);

        LocalDate today = LocalDate.now().plusDays(1);

        LocalDate oneYearAgo = today.minusYears(1);

        List<Pay> memberPay = payRepository.findOneYearData(oneYearAgo,today);
        int l = memberPay.size();
        log.info(String.valueOf(l));
        if(l==0){
            throw new NoDataExceptionHandler("결제 내역이 존재하지 않습니다");
        }

        List<PayList> payLists = new ArrayList<>();
        for(Pay pay : memberPay){
            PayList payList = new PayList();
            payList.setPayIdx(pay.getPayIdx());
            payList.setMemberId(pay.getMember().getMemberId());
            payList.setMemberName(pay.getMember().getName());
            payList.setPayDate(pay.getAuthenticatedAt().toString());
            payList.setAmount(String.valueOf(pay.getAmount()));
            payList.setContentsName(pay.getPayContents().getContentsName());
            payList.setRefundStatus(pay.getRefundStatus());
            if(pay.getRefundStatus()==Boolean.TRUE){
                if(pay.getRefundDate()==null){
                    payList.setRefundDate(LocalDate.now().toString());
                }else{
                    payList.setRefundDate(pay.getRefundDate().toString());
                }
                payList.setRefundAmount(String.valueOf(pay.getRefundAmount()));
            }
            payLists.add(payList);
        }
        Collections.reverse(payLists);

        PayListDto payListDto = new PayListDto();
        payListDto.setStatus("200");
        payListDto.setMessage("기준은 오늘로부터 1년 이내의 정보까지만 출력됩니다");
        payListDto.setData(payLists);

        return payListDto;
    }

    public MyPayListDto getPayDashBoard(String memberKey){
        memberService.checkMember(memberKey);

        Member member= memberRepository.findByMemberKey(memberKey);

        LocalDate today = LocalDate.now().plusDays(1);

        LocalDate oneYearAgo = today.minusYears(1);

        List<Pay> myPay = payRepository.findOneYearDataForMember(member,oneYearAgo,today);

        int k=myPay.size();
        if(k==0){
            throw new NoDataExceptionHandler("결제 내역이 없습니다");
        }


        List<MyPayList> myPayLists = new ArrayList<>();
        for(Pay pay : myPay){
            MyPayList myPayList = new MyPayList();
            myPayList.setPayIdx(pay.getPayIdx());
            myPayList.setContentsName(pay.getPayContents().getContentsName());
            myPayList.setOrderCode(pay.getOrderId());
            myPayList.setOrderDate(pay.getAuthenticatedAt().toString());
            myPayList.setHowTo("카드");
            myPayList.setStatus(pay.getRefundStatus());
            myPayLists.add(myPayList);
        }
        Collections.reverse(myPayLists);

        MyPayListDto myPayListDto =new MyPayListDto();
        myPayListDto.setStatus("200");
        myPayListDto.setMessage("회원 결제 정보 확인");
        myPayListDto.setData(myPayLists);

        return myPayListDto;
    }

    public MyPayDetailDto getMyDetail(String memberKey,Long payIdx){
        memberService.checkMember(memberKey);
        Member member = memberRepository.findByMemberKey(memberKey);

        Pay pay = payRepository.findByOrPayIdx(payIdx);
        if(pay==null){
            throw new NoDataExceptionHandler("결제 내역이 존재 하지 않습니다");
        }

        if(!pay.getMember().getMemberKey().equals(memberKey)){
            throw new NoDataExceptionHandler("결제 내역 접근 권한이 없습니다");
        }

        MyPayDetail myPayDetail = new MyPayDetail();
        myPayDetail.setPayIdx(pay.getPayIdx());
        myPayDetail.setContentTitle(pay.getPayContents().getContentsName());
        myPayDetail.setIntro(pay.getPayContents().getIntro());
        myPayDetail.setContentsImage(pay.getPayContents().getContentsImage());
        myPayDetail.setAmount(pay.getAmount().toString());

        MyPayDetailDto myPayDetailDto = new MyPayDetailDto();
        myPayDetailDto.setStatus("200");
        myPayDetailDto.setMessage("결제 상세 정보");
        myPayDetailDto.setData(myPayDetail);

        return myPayDetailDto;
    }

    
}
