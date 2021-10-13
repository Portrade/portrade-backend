package com.linkerbell.portradebackend.domain.save.service;

import com.linkerbell.portradebackend.domain.company.domain.Company;
import com.linkerbell.portradebackend.domain.portfolio.domain.Portfolio;
import com.linkerbell.portradebackend.domain.recruitment.domain.Recruitment;
import com.linkerbell.portradebackend.domain.save.domain.UserPortfolio;
import com.linkerbell.portradebackend.domain.save.domain.UserRecruitment;
import com.linkerbell.portradebackend.domain.save.dto.SavedPortfolioResponseDto;
import com.linkerbell.portradebackend.domain.save.dto.SavedPortfoliosResponseDto;
import com.linkerbell.portradebackend.domain.save.dto.SavedRecruitmentResponseDto;
import com.linkerbell.portradebackend.domain.save.dto.SavedRecruitmentsResponseDto;
import com.linkerbell.portradebackend.domain.save.repository.UserPortfolioRepository;
import com.linkerbell.portradebackend.domain.save.repository.UserRecruitmentRepository;
import com.linkerbell.portradebackend.domain.user.domain.Profile;
import com.linkerbell.portradebackend.domain.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class SaveServiceTest {

    @InjectMocks
    private SaveService saveService;
    @Mock
    private UserPortfolioRepository userPortfolioRepository;
    @Mock
    private UserRecruitmentRepository userRecruitmentRepository;

    User user;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .username("user1")
                .password("1234Aa@@")
                .name("김가입")
                .birthDate("12341010")
                .wantedJob("programmer")
                .profile(Profile.builder()
                        .college("college1")
                        .isGraduated(false)
                        .build())
                .build();
    }

    @Test
    @DisplayName("저장된 포트폴리오 목록 조회 성공")
    void getSavedPortfolios(){
        //given
        User savedUser = User.builder()
                .username("savedUser")
                .password("1234Aa@@")
                .build();

        Portfolio portfolio1 = Portfolio.builder()
                .user(user)
                .title("user의 포트폴리오1")
                .build();
        Portfolio portfolio2 = Portfolio.builder()
                .user(user)
                .title("user의 포트폴리오2")
                .build();
        Portfolio portfolio3 = Portfolio.builder()
                .user(user)
                .title("user의 포트폴리오3")
                .build();


        UserPortfolio userPortfolio1 = UserPortfolio.builder()
                .user(savedUser)
                .portfolio(portfolio1)
                .build();
        UserPortfolio userPortfolio2 = UserPortfolio.builder()
                .user(savedUser)
                .portfolio(portfolio2)
                .build();
        UserPortfolio userPortfolio3 = UserPortfolio.builder()
                .user(savedUser)
                .portfolio(portfolio3)
                .build();
        List<UserPortfolio> userPortfolios = new ArrayList<>(List.of(userPortfolio1, userPortfolio2, userPortfolio3));
        Page<UserPortfolio> userPortfoliosPage = new PageImpl<>(userPortfolios);

        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "id")
        );
        given(userPortfolioRepository.findByUser_Id(pageable, user.getId())).willReturn(userPortfoliosPage);

        //when
        SavedPortfoliosResponseDto savedPortfolios = saveService.getSavedPortfolios(1, 10, savedUser);

        //then
        assertEquals(savedPortfolios.getMaxPage(), 1);
        List<SavedPortfolioResponseDto> portfolios = savedPortfolios.getPortfolios();
        assertEquals(portfolios.size(), 3);
        assertEquals(portfolios.get(0).getTitle(), "user의 포트폴리오1");
        assertEquals(portfolios.get(1).getTitle(), "user의 포트폴리오2");
        assertEquals(portfolios.get(2).getTitle(), "user의 포트폴리오3");

    }

    @Test
    @DisplayName("저장된 채용공고 목록 조회 성공")
    void getSavedRecruitments(){
        //given
        User savedUser = User.builder()
                .username("savedUser")
                .password("1234Aa@@")
                .build();

        Company company = Company.builder()
                .name("company1")
                .address("서울시 강남구")
                .build();

        Recruitment recruitment1 = Recruitment.builder()
                .company(company)
                .logo("logo")
                .title("백엔드직무채용")
                .career("백엔드")
                .education("고졸이상")
                .address("서울시 강남구")
                .build();

        Recruitment recruitment2 = Recruitment.builder()
                .company(company)
                .logo("logo")
                .title("UI/UX 디자이너채용")
                .career("디자이너")
                .education("고졸이상")
                .address("서울시 강남구")
                .build();

        UserRecruitment userRecruitment1 = UserRecruitment.builder()
                .user(savedUser)
                .recruitment(recruitment1)
                .build();
        UserRecruitment userRecruitment2 = UserRecruitment.builder()
                .user(savedUser)
                .recruitment(recruitment2)
                .build();

        List<UserRecruitment> userRecruitments = new ArrayList<UserRecruitment>(List.of(userRecruitment1, userRecruitment2));
        Page<UserRecruitment> userRecruitmentPage = new PageImpl<>(userRecruitments);

        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "id")
        );

        given(userRecruitmentRepository.findByUser_Id(pageable, user.getId())).willReturn(userRecruitmentPage);

        //when
        SavedRecruitmentsResponseDto savedRecruitments = saveService.getSavedRecruitments(1, 10, savedUser);

        //then
        assertEquals(savedRecruitments.getMaxPage(), 1);
        List<SavedRecruitmentResponseDto> recruitments = savedRecruitments.getRecruitments();
        assertEquals(recruitments.size(), 2);
        assertEquals(recruitments.get(0).getTitle(), "백엔드직무채용");
        assertEquals(recruitments.get(0).getCompanyName(), "company1");
        assertEquals(recruitments.get(1).getTitle(), "UI/UX 디자이너채용");
        assertEquals(recruitments.get(1).getCompanyName(), "company1");
    }
}