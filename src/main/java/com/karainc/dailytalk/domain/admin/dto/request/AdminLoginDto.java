package com.karainc.dailytalk.domain.admin.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminLoginDto {
    private String id;
    private String password;
}
