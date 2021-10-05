package com.linkerbell.portradebackend.domain.faq.service;

import com.linkerbell.portradebackend.domain.faq.domain.Faq;
import com.linkerbell.portradebackend.domain.faq.dto.CreateFaqRequestDto;
import com.linkerbell.portradebackend.domain.faq.dto.FaqsResponseDto;
import com.linkerbell.portradebackend.domain.faq.repository.FaqRepository;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.exception.custom.NotExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FaqServiceTest {

    @InjectMocks
    private FaqService faqService;
    @Mock
    private FaqRepository faqRepository;

    User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(UUID.randomUUID())
                .username("user1")
                .password("1234Aa@@")
                .name("유저1")
                .birthDate("19900903")
                .wantedJob("designer")
                .build();
    }

    @Test
    @DisplayName("자주 묻는 질문 생성 성공")
    void createFaq() {
        //given
        CreateFaqRequestDto createFaqRequestDto = CreateFaqRequestDto
                .builder()
                .title("아이디 혹은 비밀번호를 잊었습니다.")
                .content("아이디 혹은 비밀번호를 잊은 경우, 아이디/비밀번호 찾기를 이용해 주세요.")
                .build();
        //when
        faqService.createFaq(createFaqRequestDto, user);

        //then
        verify(faqRepository, times(1))
                .save(any(Faq.class));
    }

    @Test
    @DisplayName("자주 묻는 질문 목록 조회 성공")
    void getFaqs() {
        //given
        Faq faq1 = Faq.builder()
                .user(user)
                .title("아이디 혹은 비밀번호를 잊었습니다.")
                .content("아이디 혹은 비밀번호를 잊은 경우, 아이디/비밀번호 찾기를 이용해 주세요.")
                .build();

        Faq faq2 = Faq.builder()
                .user(user)
                .title("faq2")
                .content("faq 두번쨰 글 입니다.")
                .build();

        Faq faq3 = Faq.builder()
                .user(user)
                .title("faq3")
                .content("faq 세번쨰 글 입니다.")
                .build();

        List<Faq> faqs = new ArrayList<>(Arrays.asList(faq1 ,faq2, faq3));
        Page<Faq> faqPage = new PageImpl<>(faqs);

        given(faqRepository.findAll(any(Pageable.class)))
                .willReturn(faqPage);

        //when
        FaqsResponseDto foundFaqResponseDto = faqService.getFaqs(1, 3);

        //then
        assertEquals(foundFaqResponseDto.getMaxPage(), 1);
        assertEquals(foundFaqResponseDto.getFaqs().size(), 3);
    }


    @Test
    @DisplayName("자주 묻는 질문 삭제 실패 - 존재하지 않는 ID")
    void deleteFaq_nonexistentId() {
        //given
        given(faqRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        //then
        assertThrows(NotExistException.class,
                () -> faqService.deleteFaq(1230L));
    }

    @Test
    @DisplayName("자주 묻는 질문 삭제 성공")
    void deleteFaq() {
        //given
        Faq faq = Faq.builder()
                .user(user)
                .title("아이디 혹은 비밀번호를 잊었습니다.")
                .content("아이디 혹은 비밀번호를 잊은 경우, 아이디/비밀번호 찾기를 이용해 주세요.")
                .build();

        given(faqRepository.findById(anyLong())).willReturn(Optional.of(faq));

        //when
        faqService.deleteFaq(1L);

        //then
        verify(faqRepository, times(1)).delete(any(Faq.class));
    }
}