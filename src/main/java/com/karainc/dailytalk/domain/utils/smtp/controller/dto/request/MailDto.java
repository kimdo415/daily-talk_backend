package com.karainc.dailytalk.domain.utils.smtp.controller.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailDto {
    private String email;
    private String name;
}
