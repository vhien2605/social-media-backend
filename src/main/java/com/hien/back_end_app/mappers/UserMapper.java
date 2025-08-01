package com.hien.back_end_app.mappers;


import com.hien.back_end_app.dto.request.UserCreationRequestDTO;
import com.hien.back_end_app.dto.response.post.UserRenderPostResponseDTO;
import com.hien.back_end_app.dto.response.user.UserDetailResponseDTO;
import com.hien.back_end_app.dto.response.user.UserResponseDTO;
import com.hien.back_end_app.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RoleMapper.class, AlbumMapper.class, FollowMapper.class})
public interface UserMapper {
    UserResponseDTO toDTO(User user);

    UserRenderPostResponseDTO toDTO2(User user);

    UserDetailResponseDTO toUserDetailDTO(User user);

    User toEntity(UserCreationRequestDTO dto);
}
