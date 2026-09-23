package com.karainc.dailytalk.domain.member.entity;



import com.karainc.dailytalk.domain.animation.entity.AniData;
import com.karainc.dailytalk.domain.behavior.entity.BehavResult;
import com.karainc.dailytalk.domain.board.entity.Board;
import com.karainc.dailytalk.domain.contents.entity.CheckConMember;
import com.karainc.dailytalk.domain.contents.entity.SubConMember;
import com.karainc.dailytalk.domain.language.contoller.dto.data.LangData;
import com.karainc.dailytalk.domain.language.entity.LangResult;
import com.karainc.dailytalk.domain.pay.entity.Pay;
import com.karainc.dailytalk.domain.pay.entity.PayMember;
import com.karainc.dailytalk.domain.quiz.entity.QuizData;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(unique = true)
    private String memberId;

    @Column
    private String password;

    @Column
    private String name;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickName;

    @Column
    private String memberType;


    @Column(unique = true)
    private String phoneNumber;

    @Column
    private String guardian;

    @Column
    private String memberKey;

    @Column
    private String profile;

    @Column
    private String intro;

    @Column
    private String morePn;

    @Column
    private Boolean payStatus = false;

    @Column
    private Boolean loginStatus= true;

    @Column
    private String address;


    @Column
    private String day;

    @Column
    private String birth;

    @Column
    private String sex;

    // -- 보호자 정보 --
    // 보호자 이름
    @Column
    private String parentsName;

    // 관계
    @Column
    private String relation;

    // 번호1
    @Column
    private String phone1;

    // 번호2
    @Column
    private String phone2;

    // 성별
    @Column
    private String parentSex;

    // 생년월일
    @Column
    private String parentBirth;

    @Column
    private String memberShip;

    @Column
    private String customerKey;

    @Column
    private Boolean mkService;

    @Column
    private LocalDate createDay;

    @Column
    private String loginType;

    @Column
    private Boolean dState;



    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<AniData> aniData;


    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<Board> boards;

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<CheckConMember> checkConMembers;

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<SubConMember> subConMembers;

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<Pay> pays;

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<PayMember> payMembers;


}
