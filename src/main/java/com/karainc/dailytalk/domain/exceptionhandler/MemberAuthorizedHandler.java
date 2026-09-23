package com.karainc.dailytalk.domain.exceptionhandler;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "you can't access about data")
public class MemberAuthorizedHandler extends  RuntimeException{
    private static final long serialVersionUID=1L;

    public MemberAuthorizedHandler(String message) {
        super(message);
    }
}
