package com.karainc.dailytalk.domain.member.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class LoginMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long longMemberIdx;

    @Column(name="login_date")
    private LocalDate loginDate;

    @Column(name="login_count")
    private Integer loginCount;
}
