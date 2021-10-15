package com.linkerbell.portradebackend.domain.company.repository;

import com.linkerbell.portradebackend.domain.company.domain.Company;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class CompanyRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;

    private User user;
    private Company company;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .username("users1")
                .password("1234Aa@@")
                .name("유저1")
                .birthDate("19900903")
                .wantedJob("designer")
                .build();
        userRepository.save(user);

        company = Company.builder()
                .user(user)
                .name("기업명")
                .form("스타트업")
                .industry("금융 지원 서비스업")
                .sales("1,1879억")
                .homepage("홈페이지주소")
                .memberCount("123명")
                .address("서울 강남구 테헤란로 142, 12층")
                .ceo("김기업")
                .foundingDate("2020-11-22T08:17:09.478881")
                .build();

        companyRepository.save(company);
    }

    @Test
    @DisplayName("기업명과 ceo로 회사 찾기 성공")
    void findByNameAndCeo() {
        //given
        //when
        Company foundCompany = companyRepository.findByNameAndCeo("기업명", "김기업").get();

        //then
        assertEquals(company.getName(), foundCompany.getName());
        assertEquals(company.getForm(), foundCompany.getForm());
        assertEquals(company.getIndustry(), foundCompany.getIndustry());
        assertEquals(company.getSales(), foundCompany.getSales());
        assertEquals(company.getHomepage(), foundCompany.getHomepage());
        assertEquals(company.getMemberCount(), foundCompany.getMemberCount());
        assertEquals(company.getAddress(), foundCompany.getAddress());
        assertEquals(company.getCeo(), foundCompany.getCeo());
        assertEquals(company.getFoundingDate(), foundCompany.getFoundingDate());
    }

    @Test
    @DisplayName("ID 로 회사 찾기 성공")
    void findById(){
        //given
        //when
        Company foundCompany = companyRepository.findById(this.company.getId()).get();

        //then
        assertEquals(company.getUser(), foundCompany.getUser());
        assertEquals(company.getName(), foundCompany.getName());
        assertEquals(company.getForm(), foundCompany.getForm());
        assertEquals(company.getIndustry(), foundCompany.getIndustry());
        assertEquals(company.getSales(), foundCompany.getSales());
        assertEquals(company.getHomepage(), foundCompany.getHomepage());
        assertEquals(company.getMemberCount(), foundCompany.getMemberCount());
        assertEquals(company.getAddress(), foundCompany.getAddress());
        assertEquals(company.getCeo(), foundCompany.getCeo());
        assertEquals(company.getFoundingDate(), foundCompany.getFoundingDate());
    }
}
