package com.linkerbell.portradebackend.domain.user.service;

import com.linkerbell.portradebackend.domain.user.domain.Role;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.SignUpDto;
import com.linkerbell.portradebackend.domain.user.repository.UserRepository;
import com.linkerbell.portradebackend.global.config.security.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        User user1 = User.builder()
                .username("user1")
                .name("회원1")
                .password(passwordEncoder.encode("1234"))
                .college("대학1")
                .isGraduated(true)
                .birthDate(20001214)
                .build();
        User admin1 = User.builder()
                .username("admin1")
                .name("관리자1")
                .password(passwordEncoder.encode("1234"))
                .college("대학1")
                .isGraduated(false)
                .birthDate(20001214)
                .build();
        admin1.addRole(Role.ROLE_ADMIN);

        userRepository.saveAll(Arrays.asList(user1, admin1));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(email)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자 아이디입니다."));

        return new UserAdapter(user);
    }

    @Transactional
    public User createUser(SignUpDto signUpDto) {
        if (userRepository.findByUsername(signUpDto.getId()).orElse(null) != null) {
            throw new IllegalArgumentException("이미 존재하는 사용자 아이디입니다.");
        }

        User user = User.builder()
                .username(signUpDto.getId())
                .name(signUpDto.getName())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .college(signUpDto.getCollege())
                .isGraduated(signUpDto.isGraduated())
                .wantedJob(signUpDto.getWantedJob())
                .birthDate(signUpDto.getBirthDate())
                .build();
        return userRepository.save(user);
    }
}
