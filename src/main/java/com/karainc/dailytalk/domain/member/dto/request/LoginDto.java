package com.karainc.dailytalk.domain.member.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    private String memberId;
    private String password;
}
