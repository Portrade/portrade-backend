package com.linkerbell.portradebackend.domain.user.repository;

import com.linkerbell.portradebackend.domain.user.domain.Profile;
import com.linkerbell.portradebackend.domain.user.domain.Role;
import com.linkerbell.portradebackend.domain.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .username("test1")
                .password("1234")
                .name("김회원")
                .birthDate("20030327")
                .wantedJob("marketing")
                .profile(Profile.builder()
                        .college("가나대학교")
                        .isGraduated(false)
                        .build())
                .build();
    }

    @DisplayName("사용자 엔티티 저장 성공")
    @Test
    void saveUser() throws Exception {
        // given
        // when
        User savedUser = userRepository.save(user);

        // then
        assertEquals(user.getId(), savedUser.getId());
        assertEquals(user.getUsername(), savedUser.getUsername());
        assertEquals(user.getPassword(), savedUser.getPassword());
        assertEquals(user.getName(), savedUser.getName());
        assertEquals(user.getBirthDate(), savedUser.getBirthDate());
        assertEquals(user.getWantedJob(), savedUser.getWantedJob());
        assertEquals(user.getProfile().getCollege(), savedUser.getProfile().getCollege());
        assertEquals(user.getProfile().isGraduated(), savedUser.getProfile().isGraduated());
    }

    @DisplayName("사용자 엔티티 조회 성공")
    @Test
    void findUserById() throws Exception {
        // given
        userRepository.save(user);

        // when
        User foundUser = userRepository.findById(user.getId()).get();

        // then
        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getUsername(), foundUser.getUsername());
        assertEquals(user.getPassword(), foundUser.getPassword());
        assertEquals(user.getName(), foundUser.getName());
        assertEquals(user.getBirthDate(), foundUser.getBirthDate());
        assertEquals(user.getWantedJob(), foundUser.getWantedJob());
        assertEquals(user.getProfile().getCollege(), foundUser.getProfile().getCollege());
        assertEquals(user.getProfile().isGraduated(), foundUser.getProfile().isGraduated());
    }

    @DisplayName("사용자 엔티티 삭제 성공")
    @Test
    void deleteUser() throws Exception {
        // given
        User savedUser = userRepository.save(user);

        // when
        userRepository.delete(savedUser);

        // then
        Optional<User> foundPost = userRepository.findById(savedUser.getId());
        assertEquals(Optional.empty(), foundPost);
    }

    @DisplayName("사용자 엔티티 권한 추가 성공")
    @Test
    void addRoleUser() throws Exception {
        // given
        User savedUser = userRepository.save(user);

        // when
        savedUser.addRole(Role.ROLE_ADMIN);

        // then
        assertTrue(savedUser.getRoles().contains(Role.ROLE_ADMIN));
    }

    @DisplayName("사용자 엔티티 권한 삭제 성공")
    @Test
    void deleteRoleUser() throws Exception {
        // given
        User savedUser = userRepository.save(user);

        // when
        savedUser.deleteRole(Role.ROLE_USER);

        // then
        assertFalse(savedUser.getRoles().contains(Role.ROLE_USER));
    }
}