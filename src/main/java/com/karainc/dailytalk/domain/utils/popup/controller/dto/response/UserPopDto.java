package com.karainc.dailytalk.domain.utils.popup.controller.dto.response;


import com.karainc.dailytalk.domain.utils.popup.controller.dto.data.PopupData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPopDto {
    private String status;
    private String message;
    private PopupData data;
}
