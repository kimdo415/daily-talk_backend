package com.karainc.dailytalk.domain.board.entity;


import com.karainc.dailytalk.domain.animation.entity.AniData;
import com.karainc.dailytalk.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardIdx;

    @Column
    private String boardTitle;

    @Column(columnDefinition = "TEXT")
    private String boardContent;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column
    private LocalDateTime date;

    @Column
    private String reply;

    @Column
    private boolean boardStatus;

    @Column
    private boolean replyStatus;


}
