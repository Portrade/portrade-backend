package com.linkerbell.portradebackend.domain.user.service;

import com.linkerbell.portradebackend.domain.file.domain.File;
import com.linkerbell.portradebackend.domain.file.repository.FileRepository;
import com.linkerbell.portradebackend.domain.user.domain.Profile;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.ProfileImageResponseDto;
import com.linkerbell.portradebackend.domain.user.dto.SignUpRequestDto;
import com.linkerbell.portradebackend.domain.user.dto.UserResponseDto;
import com.linkerbell.portradebackend.domain.user.repository.UserRepository;
import com.linkerbell.portradebackend.global.config.security.UserAdapter;
import com.linkerbell.portradebackend.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Util s3Util;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(email)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자 아이디입니다."));

        return new UserAdapter(user);
    }

    @Transactional
    public UserResponseDto createUser(SignUpRequestDto signUpRequestDto) {
        if (userRepository.findByUsername(signUpRequestDto.getId()).orElse(null) != null) {
            throw new IllegalArgumentException("이미 존재하는 사용자 아이디입니다.");
        }

        Profile profile = Profile.builder()
                .college(signUpRequestDto.getCollege())
                .isGraduated(signUpRequestDto.isGraduated())
                .build();

        User user = User.builder()
                .username(signUpRequestDto.getId())
                .name(signUpRequestDto.getName())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .profile(profile)
                .birthDate(signUpRequestDto.getBirthDate())
                .build();

        userRepository.save(user);

        return UserResponseDto.builder()
                .id(user.getUsername())
                .name(user.getName())
                .build();
    }

    @Transactional
    public ProfileImageResponseDto uploadProfileImage(User user, MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String filePath = s3Util.upload(file);

        File imageFile = File.builder()
                .name(originalFileName)
                .url(filePath)
                .build();

        fileRepository.save(imageFile);

        user.updateProfileImage(imageFile);

        return ProfileImageResponseDto.builder()
                .name(originalFileName)
                .url(filePath)
                .build();
    }
}
