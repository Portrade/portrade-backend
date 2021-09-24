package com.linkerbell.portradebackend.domain.user.service;

import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.SignUpRequestDto;
import com.linkerbell.portradebackend.domain.user.dto.UserResponseDto;
import com.linkerbell.portradebackend.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @Spy
    PasswordEncoder passwordEncoder;

    @Test
    public void 유저조회_존재하지않음() {
        String username = "test";
        doReturn(Optional.empty()).when(userRepository).findByUsername(username);

        final UsernameNotFoundException result = assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));

        //리팩토링 필요
        assertThat(result.getMessage()).isEqualTo("존재하지 않는 사용자 아이디입니다.");
    }

    @Test
    public void 유저조회성공() {
        String username = "test";
        doReturn(Optional.of(user())).when(userRepository).findByUsername(username);

        UserDetailsService userDetailsService = userService;
        final UserDetails result = userDetailsService.loadUserByUsername(username);

        assertThat(result.getUsername()).isEqualTo("test");
        assertThat(result.getPassword()).isEqualTo("1111");
        assertThat(result.getAuthorities()).extracting(GrantedAuthority::getAuthority)
                .contains("ROLE_USER");
    }

    //유저생성
    @Test
    public void 유저생성_이미존재함() {
        String userId = "test";
        SignUpRequestDto requestDto = SignUpRequestDto.builder()
                .userId(userId)
                .build();

        doReturn(Optional.of(user())).when(userRepository).findByUsername(userId);

        final IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> userService.createUser(requestDto));

        assertThat(result.getMessage()).isEqualTo("이미 존재하는 사용자 아이디입니다.");
    }

    @Test
    public void 유저생성성공() {
        String userId = "test";
        String name = "name";
        String password = "1111";
        SignUpRequestDto requestDto = SignUpRequestDto.builder()
                .userId(userId)
                .name(name)
                .password(password)
                .build();

        User user = user();
        user.setPassword(passwordEncoder.encode(password));
        doReturn(user).when(userRepository).save(any());

        final UserResponseDto result = userService.createUser(requestDto);

        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getName()).isEqualTo(name);

        verify(userRepository, times(1)).findByUsername(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }


    private User user() {
        return User.builder()
                .id(UUID.randomUUID())
                .username("test")
                .name("name")
                .password("1111")
                .birthDate("1993-09-09")
                .wantedJob("programming")
                .build();
    }
}