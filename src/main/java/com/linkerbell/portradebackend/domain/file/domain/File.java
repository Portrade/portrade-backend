package com.linkerbell.portradebackend.domain.file.domain;


import com.linkerbell.portradebackend.global.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString(of = {"id", "name", "url"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "file")
public class File extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    @Builder
    public File(Long id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }
}
