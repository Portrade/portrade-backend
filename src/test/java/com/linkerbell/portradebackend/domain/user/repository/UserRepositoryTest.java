package com.linkerbell.portradebackend.domain.user.repository;

import com.linkerbell.portradebackend.domain.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void 유저아이디로찾기() {
        //given
        final User user = User.builder()
                .username("idtest2")
                .password("1234")
                .name("name")
                .birthDate("1991-09-09")
                .build();

        //then
        userRepository.save(user);
        User findUser = userRepository.findByUsername("idtest2").get();

        //when
        assertThat(findUser).isNotNull();
        assertThat(findUser.getId()).isNotNull();
        assertThat(findUser.getUsername()).isEqualTo("idtest2");
        assertThat(findUser.getPassword()).isEqualTo("1234");
        assertThat(findUser.getName()).isEqualTo("name");
        assertThat(findUser.getBirthDate()).isEqualTo("1991-09-09");
    }
}