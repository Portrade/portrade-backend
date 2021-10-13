package com.linkerbell.portradebackend.domain.save.repository;

import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import com.linkerbell.portradebackend.domain.save.domain.UserPortfolio;
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
class UserPortfolioRepositoryTest {

    @Autowired
    private UserPortfolioRepository userPortfolioRepository;

    @Test
    @DisplayName("저장한 포트폴리오 조회 성공")
    void findByUserId(){
        //given
        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "id")
        );

        //when
        Page<UserPortfolio> userPortfolioPage = userPortfolioRepository.findByUser_Id(pageable, UUID.fromString("3cbe539a-33ba-4550-a82c-63be333ac2d0"));

        //then
        assertEquals(userPortfolioPage.getTotalElements(), 1);
        assertEquals(userPortfolioPage.getContent().size(), 1);

        User user = userPortfolioPage.getContent().get(0).getUser();
        Portfolio portfolio = userPortfolioPage.getContent().get(0).getPortfolio();

        assertEquals(user.getUsername(), "user1");
        assertEquals(user.getName(), "김가입");
        assertEquals(portfolio.getTitle(), "user3의 포트폴리오");
        assertEquals(portfolio.getDescription(), "안녕하세요. 포트폴리오3 소개입니다.");
    }

}