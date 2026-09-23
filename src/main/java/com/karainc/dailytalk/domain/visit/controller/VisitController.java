package com.karainc.dailytalk.domain.visit.controller;


import com.karainc.dailytalk.domain.visit.service.VisitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/visit")
@RequiredArgsConstructor
public class VisitController {
    private final VisitService visitService;

    @GetMapping
    public void getVisit(){
        visitService.getCount();
    }
}
