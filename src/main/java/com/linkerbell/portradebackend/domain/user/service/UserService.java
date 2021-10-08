package com.linkerbell.portradebackend.domain.user.service;

import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.*;
import com.linkerbell.portradebackend.domain.user.repository.UserRepository;
import com.linkerbell.portradebackend.global.config.security.UserAdapter;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.InvalidValueException;
import com.linkerbell.portradebackend.global.exception.custom.NotUniqueException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;


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

    public ExistIdResponseDto checkUsernameExists(String userId) {
        Optional<User> usernameExist = userRepository.findByUsername(userId);
        if(usernameExist.isPresent())
            throw new NotUniqueException(ErrorCode.NONUNIQUE_USERN_NAME);

        return new ExistIdResponseDto(userId);
    }
}