package com.linkerbell.portradebackend.domain.recruitment.service;

import com.linkerbell.portradebackend.domain.company.domain.Company;
import com.linkerbell.portradebackend.domain.company.dto.RecruitmentsResponseDto;
import com.linkerbell.portradebackend.domain.company.repository.CompanyRepository;
import com.linkerbell.portradebackend.domain.recruitment.domain.Recruitment;
import com.linkerbell.portradebackend.domain.recruitment.dto.RecruitmentRequestDto;
import com.linkerbell.portradebackend.domain.recruitment.repository.RecruitmentRepository;
import com.linkerbell.portradebackend.global.exception.custom.NonExistentException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RecruitmentServiceTest {

    @InjectMocks
    private RecruitmentService recruitmentService;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private RecruitmentRepository recruitmentRepository;

    private Company company;
    private Recruitment recruitment;

    @BeforeEach
    public void setup() {
        company = Company.builder().id(1L).name("(주)기업명").form("중견기업").industry("금용 지원 서비스업").sales("1,1000억(2019년 기준)").homepage("https://").memberCount("1234명").address("서울 금천구").ceo("김주이").foundingDate("2011년 2월 11일").build();
        recruitment = Recruitment.builder().logo("url.com").title("공고 제목").career("EXPERIENCED").education("HIGHSCHOOL").workType("금융 지원 서비스업").pay("2억").address("서울시 강남구 테헤란로").category("PROGRAMMING").company(company).build();
    }

    @DisplayName("기업 공고 등록 성공")
    @Test
    void createRecruitment() throws Exception {
        RecruitmentRequestDto recruitmentRequestDto = RecruitmentRequestDto.builder().logo("logo123").title("title123").career("career123").education("education123").workType("workType123").pay("pay123").address("address123").category("category123").build();

        given(companyRepository.findById(anyLong())).willReturn(Optional.ofNullable(company));

        //when
        recruitmentService.createRecruitment(1L, recruitmentRequestDto);

        //then
        verify(recruitmentRepository, times(1)).save(any(Recruitment.class));
    }

    @DisplayName("기업 정보 상세 조회 실패 - 존재하지 않는 기업 ID")
    @Test
    void getRecruitment_nonexistentCompanyId() throws Exception {
        //given
        RecruitmentRequestDto recruitmentRequestDto = RecruitmentRequestDto.builder().logo("logo123").title("title123").career("career123").education("education123").workType("workType123").pay("pay123").address("address123").category("category123").build();

        given(companyRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        //then
        assertThrows(NonExistentException.class, () -> recruitmentService.createRecruitment(1L, recruitmentRequestDto));
    }

    @DisplayName("기업 공고 목록 조회 성공")
    @Test
    void getRecruitments() throws Exception {
        //given
        Recruitment recruitment1 = Recruitment.builder().id(1L).company(company).title("백엔드 직무 채용").logo("logo1").address("서울특별시 강남구 테헤란로11길").career("백엔드").category("programmer").education("신입/경력").pay("회사내규에 따름").viewCount(1300).workType("정규직").build();
        Recruitment recruitment2 = Recruitment.builder().id(2L).company(company).title("UI/UX 디자이너 채용").logo("logo2").address("서울특별시 강남구 테헤란로11길").career("UI/UX 디자이너").category("designer").education("신입/경력").pay("회사내규에 따름").viewCount(1300).workType("정규직").build();
        Recruitment recruitment3 = Recruitment.builder().id(3L).company(company).title("프론트 직무 채용").logo("logo3").address("서울특별시 강남구 테헤란로11길").career("프론트").category("programmer").education("신입/경력").pay("회사내규에 따름").viewCount(1300).workType("정규직").build();
        List<Recruitment> recruitments = List.of(recruitment1, recruitment2, recruitment3);
        Page<Recruitment> recruitmentPage = new PageImpl<>(recruitments);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));

        given(recruitmentRepository.findAllByTitleContainingAndAddressContainingAndCareerContaining(eq(pageable), anyString(), anyString(), anyString())).willReturn(recruitmentPage);

        //when
        RecruitmentsResponseDto recruitmentsResponseDto = recruitmentService.getRecruitments(1, 10, "", "", "");

        //then
        assertEquals(recruitmentsResponseDto.getPage().getTotalPage(), 1);
        List<com.linkerbell.portradebackend.domain.company.dto.RecruitmentResponseDto> recruitmentResponseDtos = recruitmentsResponseDto.getRecruitments();
        assertEquals(recruitmentResponseDtos.get(0).getId(), 1L);
        assertEquals(recruitmentResponseDtos.get(0).getLogo(), "logo1");
        assertEquals(recruitmentResponseDtos.get(0).getTitle(), "백엔드 직무 채용");
        assertEquals(recruitmentResponseDtos.get(1).getId(), 2L);
        assertEquals(recruitmentResponseDtos.get(1).getLogo(), "logo2");
        assertEquals(recruitmentResponseDtos.get(1).getTitle(), "UI/UX 디자이너 채용");
        assertEquals(recruitmentResponseDtos.get(2).getId(), 3L);
        assertEquals(recruitmentResponseDtos.get(2).getLogo(), "logo3");
        assertEquals(recruitmentResponseDtos.get(2).getTitle(), "프론트 직무 채용");
    }

    @DisplayName("기업 공고 삭제 성공")
    @Test
    void deleteRecruitment() throws Exception {
        //given
        given(recruitmentRepository.findById(anyLong())).willReturn(Optional.of(recruitment));

        //when
        recruitmentService.deleteRecruitment(1L);

        //then
        verify(recruitmentRepository, times(1)).findById(anyLong());
        verify(recruitmentRepository, times(1)).delete(any(Recruitment.class));
    }
}