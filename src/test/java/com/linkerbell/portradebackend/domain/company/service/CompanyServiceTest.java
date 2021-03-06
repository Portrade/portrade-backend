package com.linkerbell.portradebackend.domain.company.service;

import com.linkerbell.portradebackend.domain.company.domain.Company;
import com.linkerbell.portradebackend.domain.company.dto.CompanyDetailResponseDto;
import com.linkerbell.portradebackend.domain.company.dto.CompanyRequestDto;
import com.linkerbell.portradebackend.domain.company.dto.RecruitmentResponseDto;
import com.linkerbell.portradebackend.domain.company.dto.RecruitmentsResponseDto;
import com.linkerbell.portradebackend.domain.company.repository.CompanyRepository;
import com.linkerbell.portradebackend.domain.recruitment.domain.Recruitment;
import com.linkerbell.portradebackend.domain.recruitment.repository.RecruitmentRepository;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.exception.custom.DuplicatedValueException;
import com.linkerbell.portradebackend.global.exception.custom.NonExistentException;
import com.linkerbell.portradebackend.global.exception.custom.UnAuthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @InjectMocks
    private CompanyService companyService;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private RecruitmentRepository recruitmentRepository;

    private User user;
    private Company company;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(UUID.randomUUID())
                .username("user1")
                .password("1234Aa@@")
                .name("??????1")
                .birthDate("19900903")
                .wantedJob("designer")
                .build();
        company = Company.builder()
                .id(1L)
                .name("(???)?????????")
                .form("????????????")
                .industry("?????? ?????? ????????????")
                .sales("1,1000???(2019??? ??????)")
                .homepage("https://")
                .memberCount("1234???")
                .address("?????? ?????????")
                .ceo("?????????")
                .foundingDate("2011??? 2??? 11???")
                .build();
    }

    @DisplayName("?????? ?????? ??????")
    @Test
    void createCompany() throws Exception {
        CompanyRequestDto companyRequestDto = CompanyRequestDto.builder()
                .name("(???)?????????")
                .form("????????????")
                .industry("?????? ?????? ????????????")
                .sales("1,1000???(2019??? ??????)")
                .homepage("https://")
                .memberCount("1234???")
                .address("?????? ?????????")
                .ceo("?????????")
                .foundingDate("2011??? 2??? 11???")
                .build();

        given(companyRepository.findByNameAndCeo(company.getName(), company.getCeo()))
                .willReturn(Optional.empty());

        //when
        companyService.createCompany(companyRequestDto);

        //then
        verify(companyRepository, times(1)).save(any(Company.class));
    }

    @DisplayName("?????? ?????? ?????? - ?????? ????????? ??????")
    @Test
    void createCompany_duplicatedCompany() throws Exception {
        //given
        CompanyRequestDto companyRequestDto = CompanyRequestDto.builder()
                .name("(???)?????????")
                .form("????????????")
                .industry("?????? ?????? ????????????")
                .sales("1,1000???(2019??? ??????)")
                .homepage("https://")
                .memberCount("1234???")
                .address("?????? ?????????")
                .ceo("?????????")
                .foundingDate("2011??? 2??? 11???")
                .build();

        given(companyRepository.findByNameAndCeo(company.getName(), company.getCeo()))
                .willReturn(Optional.of(company));

        //when
        //then
        assertThrows(DuplicatedValueException.class,
                () -> companyService.createCompany(companyRequestDto));
    }

    @DisplayName("?????? ?????? ?????? ?????? ?????? - ???????????? ?????? ID")
    @Test
    void getCompany_nonexistentId() throws Exception {
        //given
        given(companyRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        //then
        assertThrows(NonExistentException.class,
                () -> companyService.getCompany(1L));
    }

    @DisplayName("?????? ?????? ?????? ?????? ??????")
    @Test
    void getCompany() throws Exception {
        //given
        given(companyRepository.findById(anyLong()))
                .willReturn(Optional.of(company));

        //when
        CompanyDetailResponseDto companyDetailResponseDto = companyService.getCompany(1L);

        //then
        assertEquals(companyDetailResponseDto.getName(), company.getName());
        assertEquals(companyDetailResponseDto.getForm(), company.getForm());
        assertEquals(companyDetailResponseDto.getIndustry(), company.getIndustry());
        assertEquals(companyDetailResponseDto.getSales(), company.getSales());
        assertEquals(companyDetailResponseDto.getHomepage(), company.getHomepage());
        assertEquals(companyDetailResponseDto.getMemberCount(), company.getMemberCount());
        assertEquals(companyDetailResponseDto.getAddress(), company.getAddress());
        assertEquals(companyDetailResponseDto.getCeo(), company.getCeo());
        assertEquals(companyDetailResponseDto.getFoundingDate(), company.getFoundingDate());
    }

    @DisplayName("?????? ?????? ??????")
    @Test
    void updateCompany() throws Exception {
        //given
        CompanyRequestDto companyRequestDto = CompanyRequestDto.builder()
                .name("(???)?????????????????????")
                .form("????????????, ??????????????????")
                .industry("?????? ?????? ????????????")
                .sales("1,1879??? 3,079??????(2020??? ??????)")
                .homepage("???????????? ??????")
                .memberCount("717???(2020??? ??????)")
                .address("?????? ????????? ???????????? 142, 12???")
                .ceo("?????????")
                .foundingDate("2013??? 4??? 23???(?????? 8???)")
                .build();

        given(companyRepository.findById(anyLong()))
                .willReturn(Optional.of(company));

        //when
        companyService.updateCompany(companyRequestDto, 1L);

        //then
        verify(companyRepository, times(1)).findById(anyLong());
    }

    @DisplayName("????????? ?????? ?????? ?????? ??????")
    @Test
    void getRecruitments() throws Exception {
        //given
        Recruitment recruitment1 = Recruitment.builder()
                .id(1L)
                .company(company)
                .title("????????? ?????? ??????")
                .logo("logo1")
                .address("??????????????? ????????? ????????????11???")
                .career("?????????")
                .category("programmer")
                .education("??????/??????")
                .pay("??????????????? ??????")
                .viewCount(1300)
                .workType("?????????")
                .build();
        Recruitment recruitment2 = Recruitment.builder()
                .id(2L)
                .company(company)
                .title("UI/UX ???????????? ??????")
                .logo("logo2")
                .address("??????????????? ????????? ????????????11???")
                .career("UI/UX ????????????")
                .category("designer")
                .education("??????/??????")
                .pay("??????????????? ??????")
                .viewCount(1300)
                .workType("?????????")
                .build();
        Recruitment recruitment3 = Recruitment.builder()
                .id(3L)
                .company(company)
                .title("????????? ?????? ??????")
                .logo("logo3")
                .address("??????????????? ????????? ????????????11???")
                .career("?????????")
                .category("programmer")
                .education("??????/??????")
                .pay("??????????????? ??????")
                .viewCount(1300)
                .workType("?????????")
                .build();

        List<Recruitment> recruitments = List.of(recruitment1, recruitment2, recruitment3);
        Page<Recruitment> recruitmentPage = new PageImpl<>(recruitments);

        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "id")
        );

        given(recruitmentRepository.findAllByCompany_Id(eq(pageable), anyLong()))
                .willReturn(recruitmentPage);

        //when
        RecruitmentsResponseDto companyResponseDto = companyService.getRecruitments(1, 10, 1L);

        //then
        assertEquals(companyResponseDto.getPage().getTotalPage(), 1);

        List<RecruitmentResponseDto> recruitmentResponseDtos = companyResponseDto.getRecruitments();
        assertEquals(recruitmentResponseDtos.get(0).getId(), 1L);
        assertEquals(recruitmentResponseDtos.get(0).getLogo(), "logo1");
        assertEquals(recruitmentResponseDtos.get(0).getTitle(), "????????? ?????? ??????");
        assertEquals(recruitmentResponseDtos.get(1).getId(), 2L);
        assertEquals(recruitmentResponseDtos.get(1).getLogo(), "logo2");
        assertEquals(recruitmentResponseDtos.get(1).getTitle(), "UI/UX ???????????? ??????");
        assertEquals(recruitmentResponseDtos.get(2).getId(), 3L);
        assertEquals(recruitmentResponseDtos.get(2).getLogo(), "logo3");
        assertEquals(recruitmentResponseDtos.get(2).getTitle(), "????????? ?????? ??????");
    }

    @DisplayName("?????? ?????? ??????")
    @Test
    void deleteCompany() throws Exception {
        //given
        given(companyRepository.findById(anyLong()))
                .willReturn(Optional.of(company));

        //when
        companyService.deleteCompany(company.getId());

        //then
        verify(companyRepository, times(1)).findById(anyLong());
        verify(companyRepository, times(1)).delete(any(Company.class));
    }
}