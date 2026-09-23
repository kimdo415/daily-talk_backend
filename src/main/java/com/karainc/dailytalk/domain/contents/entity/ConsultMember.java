package com.karainc.dailytalk.domain.contents.entity;


import com.karainc.dailytalk.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConsultMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="consult_member_idx")
    private Long ConsultMemberIdx;

    @Column(name = "content_type")
    private String contentType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name = "use_count")
    private Integer useCount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Contents contents;
}
