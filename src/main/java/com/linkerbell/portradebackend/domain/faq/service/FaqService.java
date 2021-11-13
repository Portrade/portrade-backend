package com.linkerbell.portradebackend.domain.faq.service;

import com.linkerbell.portradebackend.domain.faq.domain.Faq;
import com.linkerbell.portradebackend.domain.faq.dto.CreateFaqRequestDto;
import com.linkerbell.portradebackend.domain.faq.dto.FaqResponseDto;
import com.linkerbell.portradebackend.domain.faq.dto.FaqsResponseDto;
import com.linkerbell.portradebackend.domain.faq.repository.FaqRepository;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.dto.IdResponseDto;
import com.linkerbell.portradebackend.global.common.dto.PageResponseDto;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.NonExistentException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepository;

    @Transactional
    public IdResponseDto createFaq(CreateFaqRequestDto createFaqRequestDto, User user) {
        Faq faq = createFaqRequestDto.toEntity(user);
        faqRepository.save(faq);
        return new IdResponseDto(faq.getId());
    }

    public FaqsResponseDto getFaqs(int page, int size) {
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.DESC, "id"));
        Page<Faq> faqPage = faqRepository.findAll(pageable);

        List<FaqResponseDto> faqResponseDtos = faqPage.stream()
                .map(FaqResponseDto::of)
                .collect(Collectors.toList());
        PageResponseDto pageResponseDto = PageResponseDto.builder()
                .totalPage(faqPage.getTotalPages())
                .totalElement(faqPage.getTotalElements())
                .build();

        return FaqsResponseDto.builder()
                .page(pageResponseDto)
                .faqs(faqResponseDtos)
                .build();
    }

    @Transactional
    public void deleteFaq(Long faqId) {
        Faq faq = faqRepository.findById(faqId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.NONEXISTENT_FAQ));

        faqRepository.delete(faq);
    }
}
