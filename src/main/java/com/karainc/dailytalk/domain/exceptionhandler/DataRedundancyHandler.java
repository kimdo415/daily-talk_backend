package com.karainc.dailytalk.domain.exceptionhandler;


import com.karainc.dailytalk.domain.member.dto.response.ResultDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "you can't access about data")
public class DataRedundancyHandler extends  RuntimeException{
    private static final long serialVersionUID=1L;
    public DataRedundancyHandler(String message) {
        super(message);
    }
}
