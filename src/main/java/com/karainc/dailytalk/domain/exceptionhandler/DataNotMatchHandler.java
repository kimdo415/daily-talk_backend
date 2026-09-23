package com.karainc.dailytalk.domain.exceptionhandler;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "you can't access about data")
public class DataNotMatchHandler extends  RuntimeException{
    private static final long serialVersionUID=1L;
    public DataNotMatchHandler(String message) {
        super(message);
    }
}
