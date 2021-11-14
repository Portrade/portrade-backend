package com.linkerbell.portradebackend.domain.recruitment.domain;

import com.linkerbell.portradebackend.domain.company.domain.Company;
import com.linkerbell.portradebackend.global.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString(exclude = {"company"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "recruitment")
public class Recruitment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruitment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private String logo;

    @Column(nullable = false)
    private String title;

    @Column(name = "view_count")
    private int viewCount = 0;

    @Column(nullable = false)
    private String career;

    @Column(nullable = false)
    private String education;

    @Column(nullable = false)
    private String workType;

    @Column(nullable = false)
    private String pay;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String category;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Builder
    public Recruitment(Long id, Company company, String logo, String title, int viewCount, String career, String education, String workType, String pay, String address, String category) {
        this.id = id;
        this.company = company;
        this.logo = logo;
        this.title = title;
        this.viewCount = viewCount;
        this.career = career;
        this.education = education;
        this.workType = workType;
        this.pay = pay;
        this.address = address;
        this.category = category;
    }
}
