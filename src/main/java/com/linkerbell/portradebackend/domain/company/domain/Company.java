package com.linkerbell.portradebackend.domain.company.domain;

import com.linkerbell.portradebackend.global.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString(of = {"id", "name", "form", "industry", "sales", "homepage", "memberCount", "address"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "company")
public class Company extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long id;

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

    @Column(name = "founding_date")
    private LocalDate foundingDate;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    @Builder
    public Company(Long id, String name, String form, String industry, String sales, String homepage, String memberCount, String address, LocalDate foundingDate) {
        this.id = id;
        this.name = name;
        this.form = form;
        this.industry = industry;
        this.sales = sales;
        this.homepage = homepage;
        this.memberCount = memberCount;
        this.address = address;
        this.foundingDate = foundingDate;
    }
}
