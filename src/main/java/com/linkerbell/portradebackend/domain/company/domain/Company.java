package com.linkerbell.portradebackend.domain.company.domain;

import com.linkerbell.portradebackend.global.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
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
    private LocalDate foundingDate;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    @Builder
    public Company(Long id, String name, String form, String industry, String sales, String homepage, String memberCount, String address, String ceo, LocalDate foundingDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.name = name;
        this.form = form;
        this.industry = industry;
        this.sales = sales;
        this.homepage = homepage;
        this.memberCount = memberCount;
        this.address = address;
        this.ceo = ceo;
        this.foundingDate = foundingDate;
        this.lastModifiedDate = lastModifiedDate;
    }
}
