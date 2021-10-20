package com.linkerbell.portradebackend.domain.file.domain;

import com.linkerbell.portradebackend.global.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@DiscriminatorColumn
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "file")
public class File extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "original_file_name", nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private String extension;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    protected File(Long id, String url, String fileName, String originalFileName, String extension) {
        this.id = id;
        this.url = url;
        this.fileName = fileName;
        this.originalFileName = originalFileName;
        this.extension = extension;
    }

/*  TODO remove original
    public void update() {

        lastModifiedDate = LocalDateTime.now();
    }
*/
}