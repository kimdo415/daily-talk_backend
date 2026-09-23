package com.karainc.dailytalk.domain.exceptionhandler;


import com.karainc.dailytalk.domain.member.dto.response.ResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    ResultDto resultDto = new ResultDto();
    @ExceptionHandler(DataRedundancyHandler.class)
    public ResponseEntity<ResultDto> handleRedundancyHandler(DataRedundancyHandler ex){
        log.error(ex.getMessage());
        resultDto.setStatus("400");
        resultDto.setData(ex.getMessage());
        resultDto.setMessage(ex.getMessage());
        return ResponseEntity.badRequest().body(resultDto);
    }

    @ExceptionHandler(DataNotMatchHandler.class)
    public ResponseEntity<ResultDto> dataNotMatchHandler(DataNotMatchHandler ex){
        log.error(ex.getMessage());
        resultDto.setStatus("400");
        resultDto.setData(ex.getMessage());
        resultDto.setMessage(ex.getMessage());
        return ResponseEntity.badRequest().body(resultDto);
    }

    @ExceptionHandler(DomainUnAuthorizedHandler.class)
    public ResponseEntity<ResultDto> adminUnAuthorizedHandler(DomainUnAuthorizedHandler ex){
        log.error(ex.getMessage());
        resultDto.setStatus("401");
        resultDto.setData(ex.getMessage());
        resultDto.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultDto);
    }
    @ExceptionHandler(MemberAuthorizedHandler.class)
    public ResponseEntity<ResultDto> memberNoAutHandler(MemberAuthorizedHandler ex){
        log.error(ex.getMessage());
        resultDto.setStatus("401");
        resultDto.setMessage(ex.getMessage());
        resultDto.setData(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultDto);
    }

    @ExceptionHandler(NoDataExceptionHandler.class)
    public ResponseEntity<ResultDto> noDataExceptionHandler(NoDataExceptionHandler ex){
        log.error(ex.getMessage());
        resultDto.setStatus("400");
        resultDto.setData(ex.getMessage());
        resultDto.setMessage(ex.getMessage());
        return ResponseEntity.badRequest().body(resultDto);
    }

}
