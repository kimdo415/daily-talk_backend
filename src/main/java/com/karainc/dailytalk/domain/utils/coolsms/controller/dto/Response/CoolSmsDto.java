package com.karainc.dailytalk.domain.utils.coolsms.controller.dto.Response;


import lombok.Getter;
import lombok.Setter;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;

@Getter
@Setter
public class CoolSmsDto {
    private String status;
    private String message;
    private String data;
}
