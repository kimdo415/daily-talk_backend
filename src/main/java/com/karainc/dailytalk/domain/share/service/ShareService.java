package com.karainc.dailytalk.domain.share.service;


import com.karainc.dailytalk.domain.behavior.entity.BehavResult;
import com.karainc.dailytalk.domain.behavior.repository.BehavRepository;
import com.karainc.dailytalk.domain.exceptionhandler.NoDataExceptionHandler;
import com.karainc.dailytalk.domain.language.entity.LangResult;
import com.karainc.dailytalk.domain.language.repository.LangResultRepository;
import com.karainc.dailytalk.domain.member.service.MemberService;
import com.karainc.dailytalk.domain.share.controller.dto.data.GetShareBehav;
import com.karainc.dailytalk.domain.share.controller.dto.data.GetShareLang;
import com.karainc.dailytalk.domain.share.controller.dto.requset.ShareBehavDto;
import com.karainc.dailytalk.domain.share.controller.dto.requset.ShareLangDto;
import com.karainc.dailytalk.domain.share.controller.dto.response.GetBehavDto;
import com.karainc.dailytalk.domain.share.controller.dto.response.GetLangDto;
import com.karainc.dailytalk.domain.share.controller.dto.response.ShareMsgDto;
import com.karainc.dailytalk.domain.share.entity.Share;
import com.karainc.dailytalk.domain.share.repository.ShareRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ShareService {
    private final ShareRepository shareRepository;
    private final LangResultRepository langResultRepository;
    private final BehavRepository behavRepository;
    private final MemberService memberService;

    public ShareMsgDto shareLang(String memberKey,ShareLangDto shareLangDto){
        memberService.checkMember(memberKey);

        LangResult langResult = langResultRepository.findByLangResultId(shareLangDto.getLangResultId());
        if(langResult==null){
            throw new NoDataExceptionHandler("존재하지 않는 결과입니다");
        }

        if(!(langResult.getMember().getMemberKey().equals(memberKey))){
            throw new NoDataExceptionHandler("공유권한이 없습니다");
        }

        UUID uuid = UUID.randomUUID();

        Share share = Share.builder()
                .type("lang")
                .langResult(langResult)
                .shareCode(uuid.toString())
                .build();

        shareRepository.save(share);

        ShareMsgDto shareMsgDto = new ShareMsgDto();
        shareMsgDto.setStatus("200");
        shareMsgDto.setMessage("언어문제 진단 결과 공유");
        shareMsgDto.setData(uuid.toString());
        return shareMsgDto;
    }

    public GetLangDto getShareLang(String shareCode){
        Share share = shareRepository.findByShareCode(shareCode);
        if(share==null) {
            throw new NoDataExceptionHandler("접근 불가능한 검사결과 입니다");
        }
        if(share.getLangResult()==null){
            throw new NoDataExceptionHandler("탈퇴한 회원의 게시물 입니다");
        }

        GetShareLang getShareLang =new GetShareLang();
        getShareLang.setLangResultId(share.getLangResult().getLangResultId());
        getShareLang.setName(share.getLangResult().getMember().getName());
        getShareLang.setLangPoint(share.getLangResult().getLangPoint());
        getShareLang.setLangStick(share.getLangResult().getLangStick());
        getShareLang.setLangDate(share.getLangResult().getLangDate());

        GetLangDto getLangDto = new GetLangDto();
        getLangDto.setStatus("200");
        getLangDto.setMessage("공유의 경우 다음 일정은 나오지 않습니다");
        getLangDto.setData(getShareLang);
        return getLangDto;
    }

    public ShareMsgDto deleteShareLang(String memberKey,Long langResultId){
        memberService.checkMember(memberKey);

        LangResult langResult = langResultRepository.findByLangResultId(langResultId);
        if(langResult==null){
            throw new NoDataExceptionHandler("접근 불가능한 검사결과 입니다");
        }
        Share share = shareRepository.findByLangResult(langResult);
        if(share==null){
            throw new NoDataExceptionHandler("접근 불가능한 검사결과 입니다");
        }


        shareRepository.delete(share);

        ShareMsgDto shareMsgDto = new ShareMsgDto();
        shareMsgDto.setStatus("200");
        shareMsgDto.setMessage("언어문제 진단 결과 공유 중단이 완료 되었습니다");
        shareMsgDto.setData("언어문제 진단 결과 공유 중단이 완료 되었습니다");
        return shareMsgDto;
    }




    public ShareMsgDto shareBehav(String memberKey,ShareBehavDto shareBehavDto){
        memberService.checkMember(memberKey);

        BehavResult behavResult= behavRepository.findByBehavResultId(shareBehavDto.getBehavResultId());
        if(behavResult==null){
            throw new NoDataExceptionHandler("존재하지 않는 결과입니다");
        }

        if(!(behavResult.getMember().getMemberKey().equals(memberKey))){
            throw new NoDataExceptionHandler("공유권한이 없습니다");
        }

        UUID uuid = UUID.randomUUID();

        Share share = Share.builder()
                .type("behav")
                .behavResult(behavResult)
                .shareCode(uuid.toString())
                .build();

        shareRepository.save(share);

        ShareMsgDto shareMsgDto = new ShareMsgDto();
        shareMsgDto.setStatus("200");
        shareMsgDto.setMessage("행동동문제 진단 결과 공유");
        shareMsgDto.setData(uuid.toString());
        return shareMsgDto;
    }

    public GetBehavDto shareB(String shareCode){
        Share share = shareRepository.findByShareCode(shareCode);
        if(share==null){
            throw new NoDataExceptionHandler("접근 불가능한 검사결과 입니다");
        }

        if(share.getBehavResult()==null){
            throw new NoDataExceptionHandler("탙퇴한 회원의 검사결과는 접근할 수 없습니다");
        }

        GetShareBehav getShareBehav =new GetShareBehav();
        getShareBehav.setBehavResultId(share.getBehavResult().getBehavResultId());
        getShareBehav.setName(share.getBehavResult().getMember().getName());
        getShareBehav.setBehavFive(share.getBehavResult().getBehavPoint());
        getShareBehav.setBehavStick(share.getBehavResult().getBehavStick());
        getShareBehav.setBehavDate(share.getBehavResult().getBehavDate());

        GetBehavDto getBehavDto = new GetBehavDto();
        getBehavDto.setStatus("200");
        getBehavDto.setMessage("공유의 경우 다음 일정은 보여주지 않습니다");
        getBehavDto.setData(getShareBehav);
        return getBehavDto;
    }

    public ShareMsgDto deleteShareBe(String memberKey,Long behavResultId){
        memberService.checkMember(memberKey);
        BehavResult behavResult = behavRepository.findByBehavResultId(behavResultId);

        if(behavResult==null){
            throw new NoDataExceptionHandler("접근 불가능한 검사결과 입니다");
        }

        Share share = shareRepository.findByBehavResult(behavResult);
        if(share==null){
            throw new NoDataExceptionHandler("접근 불가능한 검사결과 입니다");
        }

        shareRepository.delete(share);

        ShareMsgDto shareMsgDto = new ShareMsgDto();
        shareMsgDto.setStatus("200");
        shareMsgDto.setMessage("행동문제 진단 결과 공유 중단이 완료 되었습니다");
        shareMsgDto.setData("행동문제 진단 결과 공유 중단이 완료 되었습니다");
        return shareMsgDto;
    }
}
