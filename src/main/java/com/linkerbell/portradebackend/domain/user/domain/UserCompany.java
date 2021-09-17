package com.linkerbell.portradebackend.domain.user.domain;

import com.linkerbell.portradebackend.domain.recruit.domain.Company;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString(of = {"id"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_company")
public class UserCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_company_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Builder
    public UserCompany(Long id, User user, Company company) {
        this.id = id;
        this.user = user;
        this.company = company;
    }
}
