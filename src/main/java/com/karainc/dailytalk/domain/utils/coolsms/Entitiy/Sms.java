package com.karainc.dailytalk.domain.utils.coolsms.Entitiy;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Sms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long smsIdx;

    @Column(name="check_Pn")
    private String checkPn;

    @Column
    private String code;
}
