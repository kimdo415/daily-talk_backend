package com.karainc.dailytalk.domain.share.entity;


import com.karainc.dailytalk.domain.behavior.entity.BehavResult;
import com.karainc.dailytalk.domain.language.entity.LangResult;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Share {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="share_idx")
    private Long shareIdx;

    @Column(name="type")
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    private LangResult langResult;

    @ManyToOne(fetch = FetchType.LAZY)
    private BehavResult behavResult;

    @Column(name="share_code")
    private String shareCode;
}
