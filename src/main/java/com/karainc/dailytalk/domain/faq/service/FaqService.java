package com.karainc.dailytalk.domain.faq.service;


import com.karainc.dailytalk.domain.admin.service.AdminService;
import com.karainc.dailytalk.domain.exceptionhandler.DataNotMatchHandler;
import com.karainc.dailytalk.domain.exceptionhandler.NoDataExceptionHandler;
import com.karainc.dailytalk.domain.faq.controller.dto.request.FaqBoardDto;
import com.karainc.dailytalk.domain.faq.controller.dto.request.FaqUpdateDto;
import com.karainc.dailytalk.domain.faq.controller.dto.request.StatusDto;
import com.karainc.dailytalk.domain.faq.controller.dto.response.AllFaqDto;
import com.karainc.dailytalk.domain.faq.controller.dto.response.FaqResultDto;
import com.karainc.dailytalk.domain.faq.entity.Faq;
import com.karainc.dailytalk.domain.faq.repository.FaqRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class FaqService {

    private final FaqRepository faqRepository;
    private final AdminService adminService;

    public FaqResultDto createFaq(String adminKey, FaqBoardDto faqBoardDto){
        adminService.checkAdmin(adminKey);
        if((faqBoardDto.getTitle()==null || faqBoardDto.getTitle()=="") ||
                (faqBoardDto.getContent() == null || faqBoardDto.getContent()=="")){
            throw new DataNotMatchHandler("제목과 내용 모두 작성해야합니다");
        }
        Faq faq = Faq.builder().
                faqTitle(faqBoardDto.getTitle()).
                faqContent(faqBoardDto.getContent()).
                writer("관리자").
                date(LocalDate.now().toString()).
                view(false).
                build();
        faqRepository.save(faq);

        FaqResultDto faqResultDto = new FaqResultDto();
        faqResultDto.setStatus("200");
        faqResultDto.setMessage("관리자용 FAQ 작성 완료");
        faqResultDto.setData("관리자용 FAQ 작성 완료");
        return faqResultDto;
    }

    public FaqResultDto updateFaq(String adminKey, FaqUpdateDto faqUpdateDto){
        adminService.checkAdmin(adminKey);
        Faq faq = faqRepository.findByFaqId(faqUpdateDto.getFaqId());
        if(faq == null){
            throw new DataNotMatchHandler("삭제된 FAQ 게시물 입니다");
        }
        if((faqUpdateDto.getFaqTitle()==null || faqUpdateDto.getFaqTitle()=="") ||
                (faqUpdateDto.getFaqContent() == null || faqUpdateDto.getFaqContent()=="")){
            throw new DataNotMatchHandler("제목과 내용 모두 작성해야합니다");
        }

        faq.setFaqTitle(faqUpdateDto.getFaqTitle());
        faq.setFaqContent(faqUpdateDto.getFaqContent());
        faq.setDate(LocalDate.now().toString());
        faq.setWriter("관리자");
        faqRepository.save(faq);

        FaqResultDto faqResultDto = new FaqResultDto();
        faqResultDto.setStatus("200");
        faqResultDto.setMessage("관리자용 FAQ 수정 완료");
        faqResultDto.setData("관리자용 FAQ 수정 완료");
        return faqResultDto;
    }

    public FaqResultDto deleteFaq(String adminKey, Long faqIdx){
        adminService.checkAdmin(adminKey);
        Faq faq =faqRepository.findByFaqId(faqIdx);
        if(faq==null){
            throw new NoDataExceptionHandler("존재하지 않는 FAQ 게시물 입니다");
        }

        faqRepository.delete(faq);
        FaqResultDto faqResultDto = new FaqResultDto();
        faqResultDto.setStatus("200");
        faqResultDto.setMessage("FAQ 게시글 삭제 완료");
        faqResultDto.setData("FAQ 게시글 삭제");
        return faqResultDto;
    }

    public AllFaqDto getAllFaq(String adminKey){
        adminService.checkAdmin(adminKey);

        List<Faq> faqList= faqRepository.findAll();
        if(faqList.size()<=0 || faqList==null){
            throw new DataNotMatchHandler("FAQ 게시글이 없습니다");
        }
        AllFaqDto allFaqDto=new AllFaqDto();
        allFaqDto.setStatus("200");
        allFaqDto.setMessage("FAQ 전체 내용(관리자 권한)");
        allFaqDto.setData(faqList);
        return allFaqDto;
    }

    public FaqResultDto changeStatus(String adminKey, StatusDto statusDto){
        adminService.checkAdmin(adminKey);
        Faq faq = faqRepository.findByFaqId(statusDto.getFaqId());
        if(faq == null){
            throw new DataNotMatchHandler("삭제된 FAQ 게시물 입니다");
        }

        Boolean getView=faq.getView();
        String message;
        if(getView==Boolean.FALSE){
            getView=Boolean.TRUE;
            message="faq 게시글 공개 완료(관리자 권한)";
        }else{
            getView=Boolean.FALSE;
            message="faq 게시글 공개 중단(관리자 권한)";
        }

        faq.setView(getView);
        faqRepository.save(faq);
        FaqResultDto faqResultDto = new FaqResultDto();
        faqResultDto.setStatus("200");
        faqResultDto.setMessage(message);
        faqResultDto.setData(message);
        return faqResultDto;
    }

    public AllFaqDto getMembersFaq(){
        List<Faq> faqList= faqRepository.findByView(Boolean.TRUE);
        if(faqList.size()<=0 || faqList==null){
            throw new DataNotMatchHandler("FAQ 게시글이 없습니다");
        }

        log.info("faq 목록 조회");
        AllFaqDto allFaqDto=new AllFaqDto();
        allFaqDto.setStatus("200");
        allFaqDto.setMessage("FAQ 전체 내용 조회");
        allFaqDto.setData(faqList);
        return allFaqDto;
    }

}
