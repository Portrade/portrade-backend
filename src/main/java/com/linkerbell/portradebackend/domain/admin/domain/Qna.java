package com.linkerbell.portradebackend.domain.admin.domain;

import com.linkerbell.portradebackend.global.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString(of = {"id", "name", "phoneNumber", "title", "content"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "qna")
public class Qna extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    //문의 분류
    @Enumerated(EnumType.STRING)
    private Category category;

    //답변 완료 유무
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    @Builder
    public Qna(Long id, String name, String phoneNumber, String title, String content, Category category, Status status) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.title = title;
        this.content = content;
        this.category = category;
        this.status = status;
    }
}
