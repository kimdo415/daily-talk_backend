package com.karainc.dailytalk.domain.pay.controller.dto.response;


import com.karainc.dailytalk.domain.pay.controller.dto.data.PayList;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PayListDto {
    private String status;
    private String message;
    private List<PayList> data;
}
