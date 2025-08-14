package com.hien.back_end_app.mappers;

import com.hien.back_end_app.dto.response.report.ReportResponseDTO;
import com.hien.back_end_app.entities.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ReportMapper {
    @Mapping(source = "createAt", target = "createAt")
    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "comment.id", target = "commentId")
    ReportResponseDTO toDTO(Report report);
}
