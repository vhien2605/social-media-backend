package com.hien.back_end_app.controllers;

import com.hien.back_end_app.dto.request.UserCreationRequestDTO;
import com.hien.back_end_app.dto.response.PageResponseDTO;
import com.hien.back_end_app.dto.response.user.UserDetailResponseDTO;
import com.hien.back_end_app.dto.response.user.UserResponseDTO;
import com.hien.back_end_app.entities.User;
import com.hien.back_end_app.mappers.UserMapper;
import com.hien.back_end_app.services.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Test
    @WithMockUser(username = "hien", roles = {"SYS_ADMIN"})
    public void getUsersTest() throws Exception {
        Mockito.when(userService.getUsersPagination(Mockito.any(Pageable.class)))
                .thenReturn(PageResponseDTO.builder()
                        .pageNo(0)
                        .pageSize(1)
                        .totalPage(1)
                        .data(List.of(
                                User.builder()
                                        .email("asdasd@asdsad.com")
                                        .password("asasd")
                                        .fullName("dep trai")
                                        .build()
                        ))
                        .build());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/user/get-users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("status")
                        .value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("get users pagination successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.data", Matchers.hasSize(1)));
    }

    @Test
    @WithMockUser(username = "hien", roles = {"SYS_ADMIN"})
    public void createUserSuccessTest() throws Exception {
        User user = User.builder()
                .email("asdasd@asdsad.com")
                .password("asasd")
                .fullName("dep trai")
                .build();
        UserResponseDTO userResponseDTO = userMapper.toDTO(user);
        Mockito.when(userService.create(Mockito.any(UserCreationRequestDTO.class), Mockito.any()))
                .thenReturn(userResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/user/create")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .queryParam("data", "{\"email\":\"user@example.com\",\"password\":\"Secret123!\",\"fullName\":\"Nguyen Van A\",\"dateOfBirth\":\"1998-05-20\",\"address\":\"123 Đường ABC, Quận 1, TP.HCM\",\"education\":\"Đại học Công nghệ Thông tin\",\"work\":\"Software Engineer\",\"gender\":\"MALE\"}\n")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("status")
                        .value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("create user successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.fullName").value("dep trai"));
    }

    @Test
    @WithMockUser(username = "hien", roles = {"USER_PRO"})
    public void createUserAccessDenied() throws Exception {
        User user = User.builder()
                .email("asdasd@asdsad.com")
                .password("asasd")
                .fullName("dep trai")
                .build();
        UserResponseDTO userResponseDTO = userMapper.toDTO(user);
        Mockito.when(userService.create(Mockito.any(UserCreationRequestDTO.class), Mockito.any()))
                .thenReturn(userResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/user/create")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .queryParam("data", "{\"email\":\"user@example.com\",\"password\":\"Secret123!\",\"fullName\":\"Nguyen Van A\",\"dateOfBirth\":\"1998-05-20\",\"address\":\"123 Đường ABC, Quận 1, TP.HCM\",\"education\":\"Đại học Công nghệ Thông tin\",\"work\":\"Software Engineer\",\"gender\":\"MALE\"}\n")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Access Denied"));
    }


    @Test
    @WithMockUser(username = "hien", roles = {"USER_PRO"})
    public void getUserDetailTest() throws Exception {
        User user = User.builder()
                .email("asdasd@asdsad.com")
                .password("asasd")
                .fullName("dep trai")
                .build();
        UserDetailResponseDTO userResponseDTO = userMapper.toUserDetailDTO(user);
        Mockito.when(userService.getDetailInformation(Mockito.any(Long.class)))
                .thenReturn(userResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/user/1/detail-information")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("get user detail information successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.fullName").value("dep trai"));
    }
}
