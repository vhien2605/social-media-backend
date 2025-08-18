package com.hien.back_end_app.services;

import com.hien.back_end_app.dto.request.CreateReportRequestDTO;
import com.hien.back_end_app.dto.response.PageResponseDTO;
import com.hien.back_end_app.dto.response.report.ReportResponseDTO;
import com.hien.back_end_app.entities.*;
import com.hien.back_end_app.exceptions.AppException;
import com.hien.back_end_app.mappers.ReportMapper;
import com.hien.back_end_app.repositories.*;
import com.hien.back_end_app.utils.commons.GlobalMethod;
import com.hien.back_end_app.utils.enums.ErrorCode;
import com.hien.back_end_app.utils.enums.ReportType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReportMapper reportMapper;
    private final GroupRepository groupRepository;

    public ReportResponseDTO createReport(CreateReportRequestDTO dto) {
        String email = GlobalMethod.extractEmailFromContext();
        User createdUser = userRepository.findByEmailWithNoReferences(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Boolean isPostReport = dto.getIsPostReport();

        Report report = Report.builder()
                .description(dto.getDescription())
                .reasonType(dto.getReasonType())
                .createdUser(createdUser)
                .build();
        if (isPostReport) {
            Post targetPost = postRepository.findById(dto.getPostId())
                    .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXIST));
            if (targetPost.getCreatedBy().getEmail().equals(email)) {
                throw new AppException(ErrorCode.ACCESS_DENIED);
            }
            if (targetPost.getGroup() == null) {
                report.setReportType(ReportType.WALL_POST_REPORT);
            } else {
                report.setReportType(ReportType.GROUP_POST_REPORT);
                report.setGroup(targetPost.getGroup());
            }
            report.setPost(targetPost);
        } else {
            Comment targetComment = commentRepository.findById(dto.getCommentId())
                    .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_EXIST));
            if (targetComment.getCreatedBy().getEmail().equals(email)) {
                throw new AppException(ErrorCode.ACCESS_DENIED);
            }
            if (targetComment.getPost().getGroup() == null) {
                report.setReportType(ReportType.WALL_COMMENT_REPORT);
            } else {
                report.setReportType(ReportType.GROUP_COMMENT_REPORT);
                report.setGroup(targetComment.getPost().getGroup());
            }
            report.setComment(targetComment);
        }
        reportRepository.save(report);
        return reportMapper.toDTO(report);
    }


    public PageResponseDTO<Object> getAllReports(Pageable pageable) {
        Page<Report> reports = reportRepository.getAllReportsWithReferences(pageable);
        return PageResponseDTO.builder()
                .totalPage(reports.getTotalPages())
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .data(fromFetchedCollectionToDTOs(reports.toList()))
                .build();
    }


    public PageResponseDTO<Object> getAllGroupReports(Long groupId, Pageable pageable) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException(ErrorCode.GROUP_NOT_EXIST));
        String email = GlobalMethod.extractEmailFromContext();
        User createdUser = userRepository.findByEmailWithNoReferences(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (createdUser.getId() != group.getCreatedBy().getId()) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        Page<Report> reports = reportRepository.getAllGroupReportsWithReferences(groupId, pageable);
        return PageResponseDTO.builder()
                .totalPage(reports.getTotalPages())
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .data(fromFetchedCollectionToDTOs(reports.toList()))
                .build();
    }

    private List<ReportResponseDTO> fromFetchedCollectionToDTOs(List<Report> reports) {
        return reports.stream()
                .map(reportMapper::toDTO)
                .toList();
    }
}
