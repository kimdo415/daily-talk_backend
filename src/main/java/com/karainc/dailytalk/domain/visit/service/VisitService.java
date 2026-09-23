package com.karainc.dailytalk.domain.visit.service;


import com.karainc.dailytalk.domain.visit.entity.Visit;
import com.karainc.dailytalk.domain.visit.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
@Slf4j
public class VisitService {
    private final VisitRepository visitRepository;

    public void getCount(){
        LocalDate localDate =LocalDate.now();
        Visit visit = visitRepository.findByVisitDay(localDate);

        if(visit==null){
            log.info("오늘의 첫 방문자");
            Visit todayVisit = Visit.builder()
                    .visitDay(localDate)
                    .visitCount(1)
                    .build();
            visitRepository.save(todayVisit);
        }else{
            log.info("첫 방문자 아님");
            visit.setVisitCount(visit.getVisitCount()+1);
            visitRepository.save(visit);
        }
    }
}
