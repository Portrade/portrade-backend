package com.linkerbell.portradebackend.domain.user.service;

import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.ProfileImageResponseDto;
import com.linkerbell.portradebackend.domain.user.dto.SignUpRequestDto;
import com.linkerbell.portradebackend.domain.user.dto.SignUpResponseDto;
import com.linkerbell.portradebackend.domain.user.repository.UserRepository;
import com.linkerbell.portradebackend.global.common.dto.UploadResponseDto;
import com.linkerbell.portradebackend.global.config.security.UserAdapter;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.InvalidValueException;
import com.linkerbell.portradebackend.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.IOException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final EntityManager entityManager;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Util s3Util;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidValueException(ErrorCode.NONEXISTENT_USER));

        return new UserAdapter(user);
    }

    @Transactional
    public SignUpResponseDto createUser(SignUpRequestDto signUpRequestDto) {
        if (userRepository.findByUsername(signUpRequestDto.getUserId()).orElse(null) != null) {
            throw new InvalidValueException(ErrorCode.DUPLICATED_USER_USERNAME);
        }

        String encodedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());
        User user = signUpRequestDto.toEntity(encodedPassword);
        userRepository.save(user);

        return SignUpResponseDto.builder()
                .userId(user.getUsername())
                .name(user.getName())
                .build();
    }

    @Transactional
    public ProfileImageResponseDto uploadProfileImage(User user, MultipartFile file) throws IOException {
        UploadResponseDto uploadResponseDto = s3Util.upload(file);

        user.getProfile().updateProfileUrl(uploadResponseDto.getUrl());
        entityManager.merge(user);

        return ProfileImageResponseDto.builder()
                .fileName(uploadResponseDto.getNewFileName())
                .url(uploadResponseDto.getUrl())
                .build();
    }
}
