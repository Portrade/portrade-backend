package com.linkerbell.portradebackend.domain.recruit.domain;

import com.linkerbell.portradebackend.global.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString(of = {"id", "logo", "viewCount", "pay", "address"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "recruit")
public class Recruit extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruit_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private String logo;

    @Column(name = "view_count")
    private int viewCount = 0;

    @Enumerated(EnumType.STRING)
    private Career career;

    @Enumerated(EnumType.STRING)
    private Education education;

    @Enumerated(EnumType.STRING)
    private WorkType workType;

    @Column(nullable = false)
    private String pay;

    @Column(nullable = false)
    private String address;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    @Builder
    public Recruit(Long id, Company company, String logo, int viewCount, Career career, Education education, WorkType workType, String pay, String address) {
        this.id = id;
        this.company = company;
        this.logo = logo;
        this.viewCount = viewCount;
        this.career = career;
        this.education = education;
        this.workType = workType;
        this.pay = pay;
        this.address = address;
    }
}
