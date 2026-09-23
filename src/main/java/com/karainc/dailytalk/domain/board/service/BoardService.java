package com.karainc.dailytalk.domain.board.service;


import com.karainc.dailytalk.domain.admin.service.AdminService;
import com.karainc.dailytalk.domain.board.controller.dto.data.BoardInfoDetail;
import com.karainc.dailytalk.domain.board.controller.dto.data.BoardList;
import com.karainc.dailytalk.domain.board.controller.dto.request.BoardDto;
import com.karainc.dailytalk.domain.board.controller.dto.request.BoardOpenCloseDto;
import com.karainc.dailytalk.domain.board.controller.dto.request.ReplyDto;
import com.karainc.dailytalk.domain.board.controller.dto.request.UpdateBoardDto;
import com.karainc.dailytalk.domain.board.controller.dto.response.BoardInfoDto;
import com.karainc.dailytalk.domain.board.controller.dto.response.BoardListDto;
import com.karainc.dailytalk.domain.board.entity.Board;
import com.karainc.dailytalk.domain.board.repository.BoardRepository;
import com.karainc.dailytalk.domain.exceptionhandler.DataNotMatchHandler;
import com.karainc.dailytalk.domain.exceptionhandler.NoDataExceptionHandler;
import com.karainc.dailytalk.domain.member.entity.Member;
import com.karainc.dailytalk.domain.member.repository.MemberRepository;
import com.karainc.dailytalk.domain.member.service.MemberService;
import com.karainc.dailytalk.domain.utils.smtp.controller.dto.ResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final AdminService adminService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    public ResultDto make(String memberKey, BoardDto boardDto){
        memberService.checkMember(memberKey);
        Member member=memberRepository.findByMemberKey(memberKey);
        log.info("게시판 작성 : " + member.getMemberId() + member.getName());
        if(boardDto.getBoardContent().isEmpty() || boardDto.getBoardTitle().isEmpty()){
            throw new NoDataExceptionHandler("제목과 글을 모두 작성해주세요");
        }
        boolean getStatus;
        if(boardDto.getBoardStatus()==null){
            getStatus=false;
        }else {
            getStatus=boardDto.getBoardStatus();
        }
        Board board = Board.builder()
                .boardTitle(boardDto.getBoardTitle())
                .boardContent(boardDto.getBoardContent())
                .member(member)
                .date(LocalDateTime.now())
                .reply(null)
                .replyStatus(false)
                .boardStatus(getStatus)
                .build();
        boardRepository.save(board);
        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("1:1 문의글 작성이 완료되었습니다");
        resultDto.setData("1:1 문의글 작성이 완료되었습니다");
        return resultDto;
    }

    public void checkBoard(String memberKey,Long boardIdx){
        memberService.checkMember(memberKey);

        Member member = memberRepository.findByMemberKey(memberKey);

        Board board = boardRepository.findByBoardIdx(boardIdx);

        System.out.println(member.getIdx().toString());
        System.out.println(board.getMember().toString());
        if(!member.getIdx().toString().equals(board.getMember().getIdx().toString())){
            throw new DataNotMatchHandler("접근 불가능한 게시글 입니다");
        }
    }

    public ResultDto updateBoard(String memberKey, UpdateBoardDto updateBoardDto){
        memberService.checkMember(memberKey);
        Member member = memberRepository.findByMemberKey(memberKey);
        Board board = boardRepository.findByBoardIdx(updateBoardDto.getBoardIdx());
        if(board==null){
            throw new NoDataExceptionHandler("없는 게시글 입니다");
        }
        if(!board.getMember().getIdx().toString().equals(member.getIdx().toString())){
            throw new DataNotMatchHandler("접근 불가능한 게시글 입니다");
        }
        String getTitle;
        String getContent;
        if(updateBoardDto.getBoardContent().isEmpty()){
            getTitle=board.getBoardTitle();
        }else{
            getTitle= updateBoardDto.getBoardTitle();;
        }
        if(updateBoardDto.getBoardContent().isEmpty()){
            getContent=board.getBoardContent();
        }else {
            getContent=updateBoardDto.getBoardContent();
        }
        boolean getStatus;
        if(updateBoardDto.getBoardStatus()==null){
            getStatus=false;
        }else {
            getStatus=updateBoardDto.getBoardStatus();
        }
        log.info("게시판 수정 : " + member.getMemberId() + member.getName());
        board.setBoardTitle(getTitle);
        board.setBoardContent(getContent);
        board.setBoardStatus(getStatus);
        board.setDate(LocalDateTime.now());
        boardRepository.save(board);
        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("1:1 문의글 수정이 완료되었습니다");
        resultDto.setData("1:1 문의글 수정이 완료되었습니다");
        return resultDto;
    }

    public BoardListDto getAll(){
        List<Board> boardList = boardRepository.findAll(Sort.by(Sort.Direction.DESC,"date"));
        if(boardList==null){
            throw new NoDataExceptionHandler("작성된 1:1 게시글이 없습니다");
        }
        List<BoardList> boardLists = new ArrayList<>();
        for(Board board : boardList){
            BoardList getBoard= new BoardList();
            getBoard.setBoardIdx(board.getBoardIdx());
            getBoard.setBoardTitle(board.getBoardTitle());
            getBoard.setBoardStatus(board.isBoardStatus());
            getBoard.setReplyStatus(board.isReplyStatus());
            getBoard.setWriter(board.getMember().getName());
            getBoard.setDate(board.getDate().toString());
            boardLists.add(getBoard);
        }
        BoardListDto boardListDto = new BoardListDto();
        boardListDto.setStatus("200");
        boardListDto.setMessage("1:1 목록 조회");
        boardListDto.setData(boardLists);
        return boardListDto;
    }

    public BoardInfoDto getBoard(String memberKey,long boardIdx){
        memberService.checkMember(memberKey);
        Member member = memberRepository.findByMemberKey(memberKey);
        Board board= boardRepository.findByBoardIdx(boardIdx);
        if(board==null){
            throw new DataNotMatchHandler("없는 게시글 입니다");
        }

        if (!board.getMember().getIdx().toString().equals(member.getIdx().toString())){
            if(board.isBoardStatus()==Boolean.FALSE){
                throw new NoDataExceptionHandler("작성자와 관리자만 접근 가능한 게시글 입니다");
            }
        }
//        if(!board.getMember().getIdx().equals(member.getIdx())){
//            if(board.isBoardStatus()==Boolean.FALSE) {
//                if(!board.getMember().getIdx().toString().equals(member.getIdx().toString())){
//                    throw new NoDataExceptionHandler("작성자와 관리자만 접근 가능한 게시글 입니다");
//                }
//            }
//        }

        BoardInfoDetail boardInfoDetail = new BoardInfoDetail();
        boardInfoDetail.setBoardIdx(board.getBoardIdx());
        boardInfoDetail.setBoardTitle(board.getBoardTitle());
        boardInfoDetail.setBoardContent(board.getBoardContent());
        boardInfoDetail.setReplyStatus(board.isReplyStatus());
        boardInfoDetail.setBoardStatus(board.isBoardStatus());
        boardInfoDetail.setReply(board.getReply());
        boardInfoDetail.setDate(board.getDate().toString());
        boardInfoDetail.setWriter(board.getMember().getName());
        boardInfoDetail.setName(board.getMember().getName());

        BoardInfoDto boardInfoDto = new BoardInfoDto();
        boardInfoDto.setStatus("200");
        boardInfoDto.setMessage("1:1 게시글 조회");
        boardInfoDto.setData(boardInfoDetail);
        return boardInfoDto;
    }

    public ResultDto deleteBoard(String memberKey,long boardIdx){
        memberService.checkMember(memberKey);
        Member member = memberRepository.findByMemberKey(memberKey);
        Board board = boardRepository.findByBoardIdx(boardIdx);
        if(board==null){
            throw new NoDataExceptionHandler("없는 게시글 입니다");
        }
        if(!board.getMember().getIdx().equals(member.getIdx())){
            throw new DataNotMatchHandler("접근 불가능한 게시글 입니다");
        }
        boardRepository.delete(board);
        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("1:1 문의글 삭제 완료");
        resultDto.setData("1:1 문의글 삭제 완료");
        return resultDto;
    }



    public ResultDto getReply(String adminKey ,ReplyDto replyDto){
        adminService.checkAdmin(adminKey);
        Board board = boardRepository.findByBoardIdx(replyDto.getBoardIdx());
        if(board==null){
            throw new NoDataExceptionHandler("없는 게시글 입니다");
        }
        if(replyDto.getReply().isEmpty()){
            throw new NoDataExceptionHandler("답변을 작성해주세요");
        }
        board.setReplyStatus(true);
        board.setReply(replyDto.getReply());
        boardRepository.save(board);

        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("1:1 게시글 답변 작성 완료 (관리자 권한)");
        resultDto.setData("1:1 게시글 답변 작성 완료 (관리자 권한)");
        return resultDto;
    }

    public BoardInfoDto getBoardForAdmin(String adminKey,long boardIdx){
        adminService.checkAdmin(adminKey);

        Board board= boardRepository.findByBoardIdx(boardIdx);
        if(board==null){
            throw new NoDataExceptionHandler("없는 게시글 입니다");
        }

        BoardInfoDetail boardInfoDetail = new BoardInfoDetail();
        boardInfoDetail.setBoardIdx(board.getBoardIdx());
        boardInfoDetail.setBoardTitle(board.getBoardTitle());
        boardInfoDetail.setBoardContent(board.getBoardContent());
        boardInfoDetail.setReplyStatus(board.isReplyStatus());
        boardInfoDetail.setBoardStatus(board.isBoardStatus());
        boardInfoDetail.setReply(board.getReply());
        boardInfoDetail.setDate(board.getDate().toString());
        boardInfoDetail.setName(board.getMember().getName());

        BoardInfoDto boardInfoDto = new BoardInfoDto();
        boardInfoDto.setStatus("200");
        boardInfoDto.setMessage("1:1 게시글 조회 (관리자 권한)");
        boardInfoDto.setData(boardInfoDetail);
        return boardInfoDto;
    }

    public ResultDto AdminDelete(String adminKey, Long boardIdx){
        adminService.checkAdmin(adminKey);

        Board board = boardRepository.findByBoardIdx(boardIdx);
        if(board==null){
            throw new NoDataExceptionHandler("삭제된 게시글 입니다");
        }

        log.info("게시글 삭제 관리자권한");
        log.info("삭제된 게시글 위치 : " + boardIdx.toString());

        boardRepository.delete(board);

        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("삭제 완료: 관리자 권한");
        resultDto.setMessage("삭제 완료: 관리자 권한");

        return resultDto;
    }

    public ResultDto adminOpenClose(String adminKey, BoardOpenCloseDto boardOpenCloseDto){
        adminService.checkAdmin(adminKey);

        Board board =boardRepository.findByBoardIdx(boardOpenCloseDto.getBoardIdx());
        if(board==null){
            throw new NoDataExceptionHandler("삭제된 게시글 입니다");
        }

        String message;

        if(board.isBoardStatus()==Boolean.FALSE){
            board.setBoardStatus(Boolean.TRUE);
            message="해당 게시글을 공개처리 하였습니다";
            boardRepository.save(board);
        }else {
            board.setBoardStatus(Boolean.FALSE);
            message="해당 게시글을 비공개 처리 하였습니다";
            boardRepository.save(board);
        }

        log.info(message);
        log.info("상태 변경된 게시글 위치: " + board.getBoardIdx().toString());
        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage(message);
        resultDto.setData(message);
        return resultDto;
    }
}
