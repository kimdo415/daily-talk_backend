package com.karainc.dailytalk.domain.admin.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDto {
    private String memberId;
    private String newPassword;
}
