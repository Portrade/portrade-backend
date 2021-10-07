package com.linkerbell.portradebackend.domain.file.domain;

import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.dto.UploadResponseDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString(exclude = "user")
@DiscriminatorValue("PROFILE_IMAGE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImage extends File {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private ProfileImage(Long id, String url, String fileName, String originalFileName, String extension, User user) {
        super(id, url, fileName, originalFileName, extension);
        this.user = user;
    }

    public static ProfileImage of(UploadResponseDto uploadResponseDto, User user) {
        return ProfileImage.builder()
                .url(uploadResponseDto.getUrl())
                .fileName(uploadResponseDto.getNewFileName())
                .originalFileName(uploadResponseDto.getOriginalFileName())
                .extension(uploadResponseDto.getExtension())
                .user(user)
                .build();
    }
}
