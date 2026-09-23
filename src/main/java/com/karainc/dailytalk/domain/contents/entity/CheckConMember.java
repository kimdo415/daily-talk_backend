package com.karainc.dailytalk.domain.contents.entity;


import com.karainc.dailytalk.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckConMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="checkcon_idx")
    private Long checkConIdx;

    @Column(name = "content_type")
    private String contentType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name = "use_count")
    private Integer useCount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Contents contents;

}
