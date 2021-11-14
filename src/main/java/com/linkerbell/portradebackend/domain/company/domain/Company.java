package com.linkerbell.portradebackend.domain.company.domain;

import com.linkerbell.portradebackend.domain.company.dto.CompanyRequestDto;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "company")
public class Company extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String form;

    @Column(nullable = false)
    private String industry;

    @Column(nullable = false)
    private String sales;

    @Column(nullable = false)
    private String homepage;

    @Column(name = "member_count")
    private String memberCount;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String ceo;

    @Column(name = "founding_date")
    private String foundingDate;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Builder
    public Company(Long id, User user, String name, String form, String industry, String sales, String homepage, String memberCount, String address, String ceo, String foundingDate) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.form = form;
        this.industry = industry;
        this.sales = sales;
        this.homepage = homepage;
        this.memberCount = memberCount;
        this.address = address;
        this.ceo = ceo;
        this.foundingDate = foundingDate;
    }

    public void updateCompany(CompanyRequestDto companyRequestDto) {
        this.name = companyRequestDto.getName();
        this.form = companyRequestDto.getForm();
        this.industry = companyRequestDto.getIndustry();
        this.sales = companyRequestDto.getSales();
        this.homepage = companyRequestDto.getHomepage();
        this.memberCount = companyRequestDto.getMemberCount();
        this.address = companyRequestDto.getAddress();
        this.ceo = companyRequestDto.getCeo();
        this.foundingDate = companyRequestDto.getFoundingDate();
    }
}
