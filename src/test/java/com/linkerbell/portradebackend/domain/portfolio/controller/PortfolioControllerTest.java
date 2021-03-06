package com.linkerbell.portradebackend.domain.portfolio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkerbell.portradebackend.domain.portfolio.dto.CreatePortfolioRequestDto;
import com.linkerbell.portradebackend.global.config.WithMockPortradeUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.FileInputStream;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class PortfolioControllerTest {

    final String PREFIX_URI = "/api/v1/portfolios";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
    }

    @DisplayName("??????????????? ?????? API ??????")
    @Test
    @WithMockPortradeUser
    void createPortfolioApi() throws Exception {
        // given
        MockMultipartFile mainImageFile = new MockMultipartFile("mainImageFile",
                "files/image1.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream("src/test/resources/files/image1.png"));
        MockMultipartFile contentFile1 = new MockMultipartFile("contentFiles",
                "files/image2.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream("src/test/resources/files/image2.png"));
        MockMultipartFile contentFile2 = new MockMultipartFile("contentFiles",
                "files/image3.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream("src/test/resources/files/image3.png"));

        // when
        ResultActions result = mvc.perform(multipart(PREFIX_URI)
                .file(mainImageFile)
                .file(contentFile1)
                .file(contentFile2)
                .param("title", "??????????????? ??????")
                .param("description", "??????????????? ??????")
                .param("category", "art")
                .param("isPublic", "true")
                .characterEncoding("UTF-8"));

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("4"));
    }

    @DisplayName("??????????????? ?????? API ?????? - ????????????")
    @Test
    void createPortfolioApi_unAuthenticatedUser() throws Exception {
        // given
        CreatePortfolioRequestDto createPortfolioRequestDto = CreatePortfolioRequestDto.builder()
                .title("??????????????? ??????")
                .description("??????????????? ??????")
                .category("art")
                .isPublic(true)
                .build();

        // when
        ResultActions result = mvc.perform(post(PREFIX_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(createPortfolioRequestDto)));

        // then
        result.andExpect(status().isUnauthorized());
    }

    @DisplayName("??????????????? ?????? API ?????? - ???????????? ?????? ?????? ???")
    @Test
    @WithMockPortradeUser
    void createPortfolioApi_invalidRequest() throws Exception {
        // given
        MockMultipartFile mainImageFile = new MockMultipartFile("mainImageFile",
                "files/image1.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream("src/test/resources/files/image1.png"));
        MockMultipartFile contentFile = new MockMultipartFile("contentFiles",
                "files/image2.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream("src/test/resources/files/image2.png"));

        // when
        ResultActions result = mvc.perform(multipart(PREFIX_URI)
                .file(mainImageFile)
                .file(contentFile)
                .param("title", "??????????????? ??????")
                .param("category", "art")
                .param("isPublic", "true")
                .characterEncoding("UTF-8"));

        // then
        result.andExpect(status().isBadRequest());
    }

    @DisplayName("?????? ??????????????? ?????? ?????? API ??????")
    @Test
    void getRecentPortfoliosApi() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/recent"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalPage").value("1"))
                .andExpect(jsonPath("$.page.totalElement").value("3"))
                .andExpect(jsonPath("$.portfolios[0].id").value("3"))
                .andExpect(jsonPath("$.portfolios[1].id").value("2"))
                .andExpect(jsonPath("$.portfolios[2].id").value("1"))
                .andDo(print());
    }

    @DisplayName("?????? ?????? ??????????????? ?????? ?????? API ??????")
    @Test
    void getPortfoliosBySpecificCategoryApi() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/categories/programming"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalPage").value("1"))
                .andExpect(jsonPath("$.page.totalElement").value("2"))
                .andExpect(jsonPath("$.portfolios[0].id").value("3"))
                .andExpect(jsonPath("$.portfolios[1].id").value("1"))
                .andDo(print());
    }

    @DisplayName("??????????????? ?????? API ??????")
    @Test
    void getPortfoliosApi() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get(PREFIX_URI + "?keyword=??????&sort=dictionary&direction=desc"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalPage").value("1"))
                .andExpect(jsonPath("$.page.totalElement").value("3"))
                .andExpect(jsonPath("$.portfolios[0].id").value("3"))
                .andExpect(jsonPath("$.portfolios[1].id").value("2"))
                .andDo(print());
    }

    @DisplayName("??????????????? ?????? ?????? API ?????? - ?????? ?????????")
    @Test
    void getPortfolioApi_publicPortfolio() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/1"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.creator").value("user1"))
                .andExpect(jsonPath("$.title").value("??????????????? ??????1"))
                .andExpect(jsonPath("$.description").value("??????????????? ??????1"))
                .andExpect(jsonPath("$.category").value("programming"))
                .andExpect(jsonPath("$.isPublic").value(true))
                .andExpect(jsonPath("$.viewCount").value(16))
                .andExpect(jsonPath("$.likeCount").value(3))
                .andExpect(jsonPath("$.commentCount").value(2))
                .andExpect(jsonPath("$.mainImageFile.url").value("main_url"))
                .andExpect(jsonPath("$.contentFiles.size()").value(3))
                .andExpect(jsonPath("$.contentFiles[0].url").value("content_url1"));
    }

    @DisplayName("??????????????? ?????? ?????? API ?????? - ????????? ????????? ???????????? ?????? ??????")
    @Test
    @WithMockPortradeUser
    void getPortfolioApi_creator() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/3"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.creator").value("user1"))
                .andExpect(jsonPath("$.title").value("??????????????? ??????3"))
                .andExpect(jsonPath("$.description").value("??????????????? ??????"))
                .andExpect(jsonPath("$.category").value("programming"))
                .andExpect(jsonPath("$.isPublic").value(false))
                .andExpect(jsonPath("$.viewCount").value(13))
                .andExpect(jsonPath("$.likeCount").value(0))
                .andExpect(jsonPath("$.commentCount").value(0))
                .andExpect(jsonPath("$.mainImageFile.url").value("main_url"))
                .andExpect(jsonPath("$.contentFiles.size()").value(3))
                .andExpect(jsonPath("$.contentFiles[0].url").value("content_url1"));
    }

    @DisplayName("??????????????? ?????? ?????? API ?????? - ????????? ????????? ???????????? ?????? ?????? ??????")
    @Test
    void getPortfolioApi_notLoggedInUser() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/2"));

        // then
        result.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("C001"));
    }

    @DisplayName("??????????????? ?????? ?????? API ?????? - ????????? ????????? ???????????? ?????? ?????? ?????? ??????")
    @Test
    @WithMockPortradeUser
    void getPortfolioApi_unauthorizedUser() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/2"));

        // then
        result.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("C002"));
    }

    @DisplayName("??????????????? ?????? ?????? API ?????? - ???????????? ?????? ??????????????? ID")
    @Test
    void getPortfolioApi_nonexistentPortfolioId() throws Exception {
        // given
        // when
        ResultActions result = mvc.perform(get(PREFIX_URI + "/1234"));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("P001"));
    }
}