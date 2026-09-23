package com.karainc.dailytalk.domain.board.controller;


import com.karainc.dailytalk.domain.board.controller.dto.request.BoardDto;
import com.karainc.dailytalk.domain.board.controller.dto.request.BoardOpenCloseDto;
import com.karainc.dailytalk.domain.board.controller.dto.request.ReplyDto;
import com.karainc.dailytalk.domain.board.controller.dto.request.UpdateBoardDto;
import com.karainc.dailytalk.domain.board.controller.dto.response.BoardInfoDto;
import com.karainc.dailytalk.domain.board.controller.dto.response.BoardListDto;
import com.karainc.dailytalk.domain.board.service.BoardService;
import com.karainc.dailytalk.domain.utils.smtp.controller.dto.ResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public ResultDto make(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey,
                          @RequestBody BoardDto boardDto){
        return boardService.make(memberKey,boardDto);
    }

    @PutMapping("/update")
    public ResultDto update(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey,
                            @RequestBody UpdateBoardDto updateBoardDto){
        return boardService.updateBoard(memberKey,updateBoardDto);
    }

    @GetMapping("/update/check")
    public void checkMember(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey,
                            @RequestParam Long boardIdx){
        boardService.checkBoard(memberKey,boardIdx);
    }



    @GetMapping("/list")
    public BoardListDto getList(){
        return boardService.getAll();
    }

    @DeleteMapping("/delete")
    public ResultDto boardDelete(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey,
                                 @RequestParam long boardIdx){
        return boardService.deleteBoard(memberKey,boardIdx);
    }

    @GetMapping("/info")
    public BoardInfoDto getInfoBoard(@RequestHeader(value = "X-MEMBER-TOKEN",required = false)String memberKey,
                                     @RequestParam long boardIdx){
        return boardService.getBoard(memberKey,boardIdx);
    }

    @GetMapping("/for-admin")
    public BoardInfoDto getAdminBoard(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                                      @RequestParam long boardIdx){
        return boardService.getBoardForAdmin(adminKey,boardIdx);
    }

    @PutMapping("/reply")
    public ResultDto replyBoard(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                                @RequestBody ReplyDto replyDto){
        return boardService.getReply(adminKey,replyDto);
    }

    @DeleteMapping("/admin-delete")
    public ResultDto deleteByAdmin(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                                   @RequestParam Long boardIdx){
        return boardService.AdminDelete(adminKey,boardIdx);
    }

    @PutMapping("/admin-open-close")
    public ResultDto openCloseAdmin(@RequestHeader(value = "X-ADMIN-TOKEN",required = false)String adminKey,
                                    @RequestBody BoardOpenCloseDto boardOpenCloseDto){
        return boardService.adminOpenClose(adminKey,boardOpenCloseDto);
    }

}
