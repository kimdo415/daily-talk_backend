package com.karainc.dailytalk.domain.admin.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminIdx;

    @Column
    private String adminId;

    @Column
    private String adminPwd;

    @Column
    private String adminKey;
}
