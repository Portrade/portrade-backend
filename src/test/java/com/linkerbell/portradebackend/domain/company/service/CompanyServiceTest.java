package com.linkerbell.portradebackend.domain.company.service;

import com.linkerbell.portradebackend.domain.company.domain.Company;
import com.linkerbell.portradebackend.domain.company.dto.*;
import com.linkerbell.portradebackend.domain.company.repository.CompanyRepository;
import com.linkerbell.portradebackend.domain.recruitment.domain.Recruitment;
import com.linkerbell.portradebackend.domain.recruitment.repository.RecruitmentRepository;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.exception.custom.NonExistentException;
import com.linkerbell.portradebackend.global.exception.custom.DuplicatedValueException;
import com.linkerbell.portradebackend.global.exception.custom.UnAuthorizedException;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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
                .name("유저1")
                .birthDate("19900903")
                .wantedJob("designer")
                .build();

        company = Company.builder()
                .id(1L)
                .user(user)
                .name("(주)기업명")
                .form("중견기업")
                .industry("금용 지원 서비스업")
                .sales("1,1000억(2019년 기준)")
                .homepage("https://")
                .memberCount("1234명")
                .address("서울 금천구")
                .ceo("김주이")
                .foundingDate("2011년 2월 11일")
                .build();
    }

    @Test
    @DisplayName("기업 등록 실패 - 이미 등록된 기업")
    void createCompany_ununiqueCompany() {
        //given
        CreateCompanyRequestDto companyRequestDto = CreateCompanyRequestDto.builder()
                .name("(주)기업명")
                .form("중견기업")
                .industry("금용 지원 서비스업")
                .sales("1,1000억(2019년 기준)")
                .homepage("https://")
                .memberCount("1234명")
                .address("서울 금천구")
                .ceo("김주이")
                .foundingDate("2011년 2월 11일")
                .build();

        given(companyRepository.findByNameAndCeo(company.getName(), company.getCeo())).willReturn(Optional.of(company));

        //when
        //then
        assertThrows(DuplicatedValueException.class,
                () -> companyService.createCompany(companyRequestDto, user));
    }

    @Test
    @DisplayName("기업 등록 성공")
    void createCompany() {
        CreateCompanyRequestDto companyRequestDto = CreateCompanyRequestDto.builder()
                .name("(주)기업명")
                .form("중견기업")
                .industry("금용 지원 서비스업")
                .sales("1,1000억(2019년 기준)")
                .homepage("https://")
                .memberCount("1234명")
                .address("서울 금천구")
                .ceo("김주이")
                .foundingDate("2011년 2월 11일")
                .build();

        given(companyRepository.findByNameAndCeo(company.getName(), company.getCeo())).willReturn(Optional.empty());

        //when
        companyService.createCompany(companyRequestDto, user);

        //then
        verify(companyRepository, times(1)).save(any(Company.class));
    }

    @Test
    @DisplayName("기업 정보 상세 조회 실패 - 존재하지 않는 ID")
    void getCompany_nonexistentId() {
        //given
        given(companyRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        //then
        assertThrows(NonExistentException.class,
                () -> companyService.getCompany(1L));
    }

    @Test
    @DisplayName("기업 정보 상세 조회 성공")
    void getCompany() {
        //given
        given(companyRepository.findById(anyLong())).willReturn(Optional.of(company));

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

    @Test
    @DisplayName("기업 수정 실패 - 권한 없음")
    void updateCompany_unauthorizeduser() {
        //given
        CompanyRequestDto companyRequestDto = CompanyRequestDto.builder()
                .name("(주)수정된기업이름")
                .form("스타트업, 외부강사법인")
                .industry("금융 지원 서비스업")
                .sales("1,1879억 3,079만원(2020년 기준)")
                .homepage("홈페이지 주소")
                .memberCount("717명(2020년 기준)")
                .address("서울 강남구 테헤란로 142, 12층")
                .ceo("이승건")
                .foundingDate("2013년 4월 23일(업력 8년)")
                .build();

        User diffUser = User.builder()
                .id(UUID.randomUUID())
                .username("diffUser")
                .name("안동일")
                .build();

        given(companyRepository.findById(anyLong())).willReturn(Optional.of(company));

        //when
        //then
        assertThrows(UnAuthorizedException.class, () -> companyService.updateCompany(companyRequestDto,1L, diffUser));
    }

    @Test
    @DisplayName("기업 수정 성공")
    void updateCompany() {
        //given
        CompanyRequestDto companyRequestDto = CompanyRequestDto.builder()
                .name("(주)수정된기업이름")
                .form("스타트업, 외부강사법인")
                .industry("금융 지원 서비스업")
                .sales("1,1879억 3,079만원(2020년 기준)")
                .homepage("홈페이지 주소")
                .memberCount("717명(2020년 기준)")
                .address("서울 강남구 테헤란로 142, 12층")
                .ceo("이승건")
                .foundingDate("2013년 4월 23일(업력 8년)")
                .build();

        given(companyRepository.findById(anyLong())).willReturn(Optional.of(company));

        //when
        companyService.updateCompany(companyRequestDto,1L, user);

        //then
        verify(companyRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("기업의 모든 공고 조회 성공")
    void getRecruitments() {
        //given
        Recruitment recruitment1 = Recruitment.builder()
                .id(1L)
                .company(company)
                .title("백엔드 직무 채용")
                .logo("logo1")
                .address("서울특별시 강남구 테헤란로11길")
                .career("백엔드")
                .category("programmer")
                .education("신입/경력")
                .pay("회사내규에 따름")
                .viewCount(1300)
                .workType("정규직")
                .build();

        Recruitment recruitment2 = Recruitment.builder()
                .id(2L)
                .company(company)
                .title("UI/UX 디자이너 채용")
                .logo("logo2")
                .address("서울특별시 강남구 테헤란로11길")
                .career("UI/UX 디자이너")
                .category("designer")
                .education("신입/경력")
                .pay("회사내규에 따름")
                .viewCount(1300)
                .workType("정규직")
                .build();

        Recruitment recruitment3 = Recruitment.builder()
                .id(3L)
                .company(company)
                .title("프론트 직무 채용")
                .logo("logo3")
                .address("서울특별시 강남구 테헤란로11길")
                .career("프론트")
                .category("programmer")
                .education("신입/경력")
                .pay("회사내규에 따름")
                .viewCount(1300)
                .workType("정규직")
                .build();

        List<Recruitment> companys = new ArrayList<>(List.of(recruitment1,recruitment2,recruitment3));
        Page page = new PageImpl(companys);

        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "id")
        );

        given(recruitmentRepository.findAllByCompany_Id(eq(pageable), anyLong())).willReturn(page);

        //when
        CompanyRecruitmentResponseDto companyResponseDto = companyService.getRecruitments(1, 10, 1L);

        //then
        assertEquals(companyResponseDto.getMaxPage(), 1);

        List<RecruitmentResponseDto> recruitments = companyResponseDto.getRecruitments();
        assertEquals(recruitments.get(0).getId(), 1L);
        assertEquals(recruitments.get(0).getLogo(), "logo1");
        assertEquals(recruitments.get(0).getTitle(), "백엔드 직무 채용");
        assertEquals(recruitments.get(1).getId(), 2L);
        assertEquals(recruitments.get(1).getLogo(), "logo2");
        assertEquals(recruitments.get(1).getTitle(), "UI/UX 디자이너 채용");
        assertEquals(recruitments.get(2).getId(), 3L);
        assertEquals(recruitments.get(2).getLogo(), "logo3");
        assertEquals(recruitments.get(2).getTitle(), "프론트 직무 채용");
    }

    @Test
    @DisplayName("기업 삭제 실패 - 권한 없음")
    void deleteCompany_unauthorizeduser() {
        //given
        User diffUser = User.builder()
                .id(UUID.randomUUID())
                .username("diffUser")
                .name("안동일")
                .build();

        given(companyRepository.findById(anyLong())).willReturn(Optional.of(company));

        //when
        //then
        assertThrows(UnAuthorizedException.class,
                () -> companyService.deleteCompany(company.getId(), diffUser));
    }

    @Test
    @DisplayName("기업 삭제 성공")
    void deleteCompany() {
        //given
        given(companyRepository.findById(anyLong())).willReturn(Optional.of(company));

        //when
        companyService.deleteCompany(company.getId(), user);

        //then
        verify(companyRepository, times(1)).findById(anyLong());
        verify(companyRepository, times(1)).delete(any(Company.class));
    }
}