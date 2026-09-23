package com.karainc.dailytalk.domain.dashboard.service;


import com.karainc.dailytalk.domain.admin.service.AdminService;
import com.karainc.dailytalk.domain.dashboard.controller.dto.data.DashBoardDto;
import com.karainc.dailytalk.domain.dashboard.controller.dto.data.Day;
import com.karainc.dailytalk.domain.dashboard.controller.dto.data.Month;
import com.karainc.dailytalk.domain.dashboard.controller.dto.data.Week;
import com.karainc.dailytalk.domain.dashboard.controller.dto.data.pay.OneDay;
import com.karainc.dailytalk.domain.dashboard.controller.dto.data.pay.OneMonth;
import com.karainc.dailytalk.domain.dashboard.controller.dto.data.pay.OneWeek;
import com.karainc.dailytalk.domain.dashboard.controller.dto.data.pay.TotalPay;
import com.karainc.dailytalk.domain.dashboard.controller.dto.requset.DashDto;
import com.karainc.dailytalk.domain.dashboard.controller.dto.requset.DashPayDto;
import com.karainc.dailytalk.domain.member.entity.LoginMember;
import com.karainc.dailytalk.domain.member.entity.Member;
import com.karainc.dailytalk.domain.member.repository.LoginMemberRepository;
import com.karainc.dailytalk.domain.member.repository.MemberRepository;
import com.karainc.dailytalk.domain.pay.entity.Pay;
import com.karainc.dailytalk.domain.pay.repository.PayRepository;
import com.karainc.dailytalk.domain.visit.entity.Visit;
import com.karainc.dailytalk.domain.visit.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class DashBoardService {
    private final MemberRepository memberRepository;
    private final PayRepository payRepository;
    private final LoginMemberRepository loginMemberRepository;
    private final VisitRepository visitRepository;

    private final AdminService adminService;
    public DashDto getDashboard(String adminKey) {
        adminService.checkAdmin(adminKey);

        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusWeeks(1);

        // 오늘을 기준으로 일일 조ㅎ
        List<Member> dailyMembers = memberRepository.findByCreateDay(today);
        int dailyCount = dailyMembers.size();

        LoginMember todayMember =loginMemberRepository.findByLoginDate(today);
        Integer todayLogin;
        if(todayMember==null){
            todayLogin=0;
        }else{
            todayLogin=todayMember.getLoginCount();
        }

        List<Pay> todayTake = payRepository.findByAuthenticatedAt(today);
        int todayTotal=0;
        int k=todayTake.size();
        if(k!=0){
            for(Pay pay : todayTake){
                todayTotal = todayTotal + pay.getAmount();
            }
        }

        Visit visit = visitRepository.findByVisitDay(today);
        int todayVisit=0;
        if(visit !=null){
            todayVisit=visit.getVisitCount();
        }




        // 일주일 동안의 데이터
        List<Member> weeklyMembers = memberRepository.findWithinAWeek(weekAgo, today);
        int weeklyCount = weeklyMembers.size();

        List<LoginMember> weekMember = loginMemberRepository.findWithinAWeek(weekAgo,today);
        Integer weekLogin=0;
        int wk = weekMember.size();
        if(wk !=0){
            for(LoginMember loginMember :weekMember){
                weekLogin = weekLogin+loginMember.getLoginCount();
            }
        }

        List<Pay> weeklyTake = payRepository.findWithinAWeek(weekAgo,today);
        int weekTotal = 0;
        int wkt = weeklyTake.size();
        if(wkt !=0){
            for(Pay pay :weeklyTake){
                weekTotal = weekTotal+pay.getAmount();
            }
        }

        List<Visit> weekVisit = visitRepository.findWithinAWeek(weekAgo,today);
        int weekV=0;
        int wkV = weekVisit.size();
        if(wkV !=0){
            for(Visit week : weekVisit){
                weekV = weekV + week.getVisitCount();
            }
        }



        // 해당월의 데이타
        List<Member> monthMembers = memberRepository.findByCurrentMonth(today);
        int monthCount = monthMembers.size();

        List<LoginMember> monthMember = loginMemberRepository.findByCurrentMonth(today);
        Integer monthLogin=0;
        int mtm = monthMember.size();
        if(mtm !=0){
            for(LoginMember loginMember : monthMember){
                monthLogin = monthLogin+loginMember.getLoginCount();
            }
        }

        List<Pay> monthTake = payRepository.findByCurrentMonth(today);
        int monthTotal=0;
        int mtk = monthTake.size();
        if(mtk !=0){
            for(Pay pay : monthTake){
                monthTotal = monthTotal+pay.getAmount();
            }
        }

        List<Visit> monthVisit = visitRepository.findByCurrentMonth(today);
        int monthV=0;
        int montL = monthVisit.size();
        if(montL !=0){
            for(Visit month : monthVisit){
                monthV = monthV + month.getVisitCount();
            }
        }



        DashDto dashDto = new DashDto();

        Day day = new Day();
        day.setNewMembers(String.valueOf(dailyCount));
        day.setLoginMember(String.valueOf(todayLogin));
        day.setTake(String.valueOf(todayTotal));
        day.setVisit(String.valueOf(todayVisit));

        Week week = new Week();
        week.setNewMembers(String.valueOf(weeklyCount));
        week.setLoginMember(String.valueOf(weekLogin));
        week.setTake(String.valueOf(weekTotal));
        week.setVisit(String.valueOf(weekV));


        Month month = new Month();
        month.setNewMembers(String.valueOf(monthCount));
        month.setLoginMember(String.valueOf(monthLogin));
        month.setTake(String.valueOf(monthTotal));
        month.setVisit(String.valueOf(monthV));

        DashBoardDto dashBoardDto = new DashBoardDto();
        dashBoardDto.setDay(day);
        dashBoardDto.setWeek(week);
        dashBoardDto.setMonth(month);

        dashDto.setStatus("200");
        dashDto.setMessage("기준은 오늘을 기준으로 금일,금일로부터 7일 전까지,해당월 입니다");
        dashDto.setData(dashBoardDto);
        return dashDto;
    }

    public void getRandom(){
        List<Member> members = memberRepository.findAll();
        LocalDate localDate = LocalDate.now();
        for(Member member : members){
            member.setCreateDay(localDate);
            memberRepository.save(member);
        }
    }

    public DashPayDto getDashPay(String adminKey){
        adminService.checkAdmin(adminKey);

        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusWeeks(1);

        List<Pay> todayTake = payRepository.findByAuthenticatedAt(today);
        int dayTake=0;
        int dayRefund=0;
        int dayTotal=0;

        int k=todayTake.size();
        if(k!=0){
            for(Pay pay : todayTake){
                dayTake = dayTake+pay.getAmount();
                if(pay.getRefundStatus()==Boolean.TRUE){
                    dayRefund = dayRefund +pay.getRefundAmount();
                }
            }
            dayTotal = dayTake-dayRefund;
        }

        List<Pay> weeklyTake = payRepository.findWithinAWeek(weekAgo,today);
        int weekTake = 0;
        int weekRefund=0;
        int weekTotal=0;
        int wkt = weeklyTake.size();
        if(wkt !=0){
            for(Pay pay :weeklyTake){
                weekTake=weekTake+ pay.getAmount();
                if(pay.getRefundStatus()==Boolean.TRUE){
                    weekRefund = weekRefund+pay.getRefundAmount();
                }
            }
            weekTotal=weekTake-weekRefund;
        }

        List<Pay> monthTake = payRepository.findByCurrentMonth(today);
        int monthTotal=0;
        int monthPay=0;
        int monthRefund=0;

        int mtk = monthTake.size();
        if(mtk !=0){
            for(Pay pay : monthTake){
                monthPay = monthPay+pay.getAmount();
                if(pay.getRefundStatus()==Boolean.TRUE){
                    monthRefund = monthRefund +pay.getRefundAmount();
                }
            }
            monthTotal=monthPay-monthRefund;
        }

        OneDay oneDay = new OneDay();
        oneDay.setTake(String.valueOf(dayTake));
        oneDay.setRefund(String.valueOf(dayRefund));
        oneDay.setTotal(String.valueOf(dayTotal));

        OneWeek oneWeek = new OneWeek();
        oneWeek.setTake(String.valueOf(weekTake));
        oneWeek.setRefund(String.valueOf(weekRefund));
        oneWeek.setTotal(String.valueOf(weekTotal));

        OneMonth oneMonth = new OneMonth();
        oneMonth.setTake(String.valueOf(monthPay));
        oneMonth.setRefund(String.valueOf(monthRefund));
        oneMonth.setTotal(String.valueOf(monthTotal));

        TotalPay totalPay = new TotalPay();
        totalPay.setDay(oneDay);
        totalPay.setWeek(oneWeek);
        totalPay.setMonth(oneMonth);

        DashPayDto dashPayDto = new DashPayDto();
        dashPayDto.setStatus("200");
        dashPayDto.setMessage("매출 정보 기준은 오늘로부터 일일/1주/월 입니다");
        dashPayDto.setData(totalPay);

        return dashPayDto;

    }
}
