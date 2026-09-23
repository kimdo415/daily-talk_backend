package com.karainc.dailytalk.domain.exceptionhandler;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "you can't access about data")
public class NoDataExceptionHandler extends  RuntimeException{
    private static final long serialVersionUID=1L;
    public NoDataExceptionHandler(String message) {
        super(message);
    }
}
