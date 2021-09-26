package com.linkerbell.portradebackend.domain.user.controller;

import com.google.gson.Gson;
import com.linkerbell.portradebackend.domain.user.domain.User;
import com.linkerbell.portradebackend.domain.user.dto.ProfileImageResponseDto;
import com.linkerbell.portradebackend.domain.user.dto.SignUpRequestDto;
import com.linkerbell.portradebackend.domain.user.dto.UserResponseDto;
import com.linkerbell.portradebackend.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;
    private Gson gson;

    @BeforeEach
    public void init() {
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .build();
    }

    @Test
    public void 회원가입실패_올바르지않은_userId_형식() throws Exception {
        final String url = "/api/v1/users";
        final ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(gson.toJson(signUpRequestDto("IDD", "test", "Lm11200!", "programming", "19910909")))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andDo(print());
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입실패_올바르지않은_name_형식() throws Exception {
        final String url = "/api/v1/users";
        final ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(gson.toJson(signUpRequestDto("id", "t", "Lm11200!", "programming", "19910909")))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andDo(print());
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입실패_올바르지않은_password_형식() throws Exception {
        final String url = "/api/v1/users";
        final ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(gson.toJson(signUpRequestDto("id", "test", "1111", "programming", "19910909")))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andDo(print());
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입실패_희망직무가Null() throws Exception {
        final String url = "/api/v1/users";
        final ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(gson.toJson(signUpRequestDto("id", "t", "Lm11200!", null, "19910909")))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andDo(print());
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입실패_올바르지않은_birthDate_형식() throws Exception {
        final String url = "/api/v1/users";
        final ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(gson.toJson(signUpRequestDto("id", "t", "Lm11200!", "programming", "birthDate")))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andDo(print());
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입성공() throws Exception {
        final String url = "/api/v1/users";
        final UserResponseDto userResponseDto = UserResponseDto.builder()
                .userId("userid")
                .name("name")
                .build();

        doReturn(userResponseDto).when(userService).createUser(any(SignUpRequestDto.class));

        final ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(gson.toJson(signUpRequestDto("userid", "name", "Lm112000!", "programming", "19910909")))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andDo(print());
        resultActions.andExpect(status().isCreated());
        final UserResponseDto response = gson.fromJson(resultActions.andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), UserResponseDto.class);

        assertThat(response.getUserId()).isEqualTo("userid");
        assertThat(response.getName()).isEqualTo("name");
    }

//    @Test
//    @WithAnonymousUser
//    public void 프로필등록_인증되지않은사용자() throws Exception {
//
//    }

    @Test
    @WithMockUser(username = "june", roles = "ROLE_USER")
    public void 프로필등록성공() throws Exception {
        final String url = "/api/v1/users/profile/image";

        final User user = User.builder()
                .username("june")
                .build();

        MockMultipartFile file = new MockMultipartFile("imagefile", "imagefile.jpg", "image/jpeg", "imagefile".getBytes());
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(url);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        ProfileImageResponseDto profileImageResponseDto = ProfileImageResponseDto.builder()
                .fileName(file.getName())
                .build();

        doReturn(profileImageResponseDto).when(userService).uploadProfileImage(eq(user), eq(file));

        final ResultActions resultActions = mockMvc.perform(builder
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA));

        resultActions.andDo(print());
        resultActions.andExpect(status().isOk());

        verify(userService).uploadProfileImage(eq(user),any(MultipartFile.class));
    }


    private SignUpRequestDto signUpRequestDto(String userId, String name, String password, String wantedJob, String birthdate) {
        return SignUpRequestDto.builder()
                .userId(userId)
                .name(name)
                .password(password)
                .wantedJob(wantedJob)
                .birthDate(birthdate)
                .build();
    }
}