package com.linkerbell.portradebackend.domain.faq.service;

import com.linkerbell.portradebackend.domain.faq.domain.Faq;
import com.linkerbell.portradebackend.domain.faq.dto.CreateFaqRequestDto;
import com.linkerbell.portradebackend.domain.faq.dto.FaqDetailResponseDto;
import com.linkerbell.portradebackend.domain.faq.dto.FaqResponseDto;
import com.linkerbell.portradebackend.domain.faq.dto.FaqsResponseDto;
import com.linkerbell.portradebackend.domain.faq.repository.FaqRepository;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.global.common.dto.CreateResponseDto;
import com.linkerbell.portradebackend.global.exception.ErrorCode;
import com.linkerbell.portradebackend.global.exception.custom.NotExistException;
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
    public CreateResponseDto createFaq(CreateFaqRequestDto createFaqRequestDto, User user) {
        Faq faq = createFaqRequestDto.toEntity(user);
        faqRepository.save(faq);
        return new CreateResponseDto(faq.getId());
    }

    public FaqsResponseDto getFaqs(int page, int size) {
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.DESC, "id"));

        Page<Faq> pageFaqs = faqRepository.findAll(pageable);
        List<FaqResponseDto> faqs = pageFaqs.stream()
                .map(faq -> FaqResponseDto.of(faq))
                .collect(Collectors.toList());

        return FaqsResponseDto.builder()
                .faqs(faqs)
                .maxPage(pageFaqs.getTotalPages())
                .build();
    }

    public FaqDetailResponseDto getFaq(Long faqId) {
        Faq faq = faqRepository.findById(faqId)
                .orElseThrow(() -> new NotExistException(ErrorCode.NONEXISTENT_FAQ));
        return FaqDetailResponseDto.builder()
                .content(faq.getContent())
                .build();
    }

    public void deleteFaq(Long faqId) {
        Faq faq = faqRepository.findById(faqId)
                .orElseThrow(() -> new NotExistException(ErrorCode.NONEXISTENT_FAQ));

        faqRepository.delete(faq);
    }
}
