package com.linkerbell.portradebackend.domain.user.service;

import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import com.linkerbell.portradebackend.domain.portfolio.repository.PortfolioRepository;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.*;
import com.linkerbell.portradebackend.domain.user.repository.FollowRepository;
import com.linkerbell.portradebackend.domain.user.repository.UserRepository;
import com.linkerbell.portradebackend.global.common.dto.UploadResponseDto;
import com.linkerbell.portradebackend.global.config.security.UserAdapter;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.InvalidValueException;
import com.linkerbell.portradebackend.global.exception.custom.NotExistException;
import com.linkerbell.portradebackend.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
}