package com.linkerbell.portradebackend.domain.user.service;

import com.linkerbell.portradebackend.domain.user.domain.Profile;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.SignUpRequestDto;
import com.linkerbell.portradebackend.domain.user.dto.SignUpResponseDto;
import com.linkerbell.portradebackend.domain.user.repository.UserRepository;
import com.linkerbell.portradebackend.global.config.security.UserAdapter;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.DuplicatedValueException;
import com.linkerbell.portradebackend.global.exception.custom.InvalidValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidValueException(ErrorCode.NONEXISTENT_USER));

        return new UserAdapter(user);
    }

    @Transactional
    public SignUpResponseDto createUser(SignUpRequestDto signUpRequestDto) {
        if (userRepository.findByUsername(signUpRequestDto.getUserId()).orElse(null) != null) {
            throw new DuplicatedValueException(ErrorCode.DUPLICATED_USER_USERNAME);
        }

        String encodedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());
        User user = User.builder()
                .username(signUpRequestDto.getUserId())
                .name(signUpRequestDto.getName())
                .password(encodedPassword)
                .profile(Profile.builder()
                        .college(signUpRequestDto.getCollege())
                        .isGraduated(signUpRequestDto.isGraduation())
                        .build())
                .wantedJob(signUpRequestDto.getWantedJob())
                .birthDate(signUpRequestDto.getBirthDate())
                .build();
        userRepository.save(user);

        return SignUpResponseDto.builder()
                .id(user.getUsername())
                .name(user.getName())
                .build();
    }

    public void checkUsernameExists(String userId) {
        if (userRepository.findByUsername(userId).orElse(null) != null) {
            throw new DuplicatedValueException(ErrorCode.DUPLICATED_USER_USERNAME);
        }
    }
}