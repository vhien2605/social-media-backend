package com.hien.back_end_app.mappers;

import com.hien.back_end_app.dto.response.user.FollowResponseDTO;
import com.hien.back_end_app.entities.Follow;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FollowMapper {
    @Mapping(source = "followUser.id", target = "followId")
    @Mapping(source = "followUser.imageUrl", target = "imageUrl")
    @Mapping(source = "followUser.fullName", target = "fullName")
    FollowResponseDTO toDTO(Follow follow);
}
