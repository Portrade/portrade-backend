package com.linkerbell.portradebackend.domain.user.service;

import com.linkerbell.portradebackend.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    //실패하는 테스트 코드 부터 작성하자
    //존재하지 않는 사용자인 경우
    @Test
    public void 유저조회_존재하지않음() {
        String username = "test";
        doReturn(null).when(userRepository).findByUsername(username);

        final UsernameNotFoundException result = assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));

        //리팩토링 필요
        assertThat(result.getMessage()).isEqualTo("이미 존재하는 사용자 아이디입니다.");
    }

}