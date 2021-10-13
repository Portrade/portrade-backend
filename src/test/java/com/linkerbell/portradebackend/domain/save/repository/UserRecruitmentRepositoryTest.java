package com.linkerbell.portradebackend.domain.save.repository;

import com.linkerbell.portradebackend.domain.recruitment.domain.Recruitment;
import com.linkerbell.portradebackend.domain.save.domain.UserRecruitment;
import com.linkerbell.portradebackend.domain.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRecruitmentRepositoryTest {

    @Autowired
    private UserRecruitmentRepository userRecruitmentRepository;

    @Test
    @DisplayName("저장한 채용 공고 조회 성공")
    void test(){
        //given
        //given
        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "id")
        );

        //when
        Page<UserRecruitment> userRecruitmentPage = userRecruitmentRepository.findByUser_Id(pageable, UUID.fromString("3cbe539a-33ba-4550-a82c-63be333ac2d0"));

        //then
        assertEquals(userRecruitmentPage.getTotalElements(), 1);

        Recruitment recruitment = userRecruitmentPage.getContent().get(0).getRecruitment();
        User user = userRecruitmentPage.getContent().get(0).getUser();

        assertEquals(recruitment.getTitle(), "29샵 백엔드 프로그래머 채용");
        assertEquals(recruitment.getCompany().getName(), "29샵");
        assertEquals(user.getUsername(), "user1");
        assertEquals(user.getName(), "김가입");
    }
}