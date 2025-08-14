package com.hien.back_end_app.controllers;


import com.hien.back_end_app.dto.request.CreateReportRequestDTO;
import com.hien.back_end_app.dto.response.ApiResponse;
import com.hien.back_end_app.dto.response.ApiSuccessResponse;
import com.hien.back_end_app.services.ReportService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/report")
@RequiredArgsConstructor
@Validated
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/create-report")
    public ApiResponse createReport(@RequestBody @Valid CreateReportRequestDTO dto) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("created report")
                .data(reportService.createReport(dto))
                .build();
    }


    @GetMapping("/all-reports")
    @PreAuthorize("hasRole('SYS_ADMIN')")
    public ApiResponse getAllReports(Pageable pageable) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get all reports")
                .data(reportService.getAllReports(pageable))
                .build();
    }

    @GetMapping("/all-group-reports/{groupId}")
    public ApiResponse getGroupReports(
            Pageable pageable,
            @PathVariable @Min(value = 0, message = "group id must be greater than 0") Long groupId) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("get all group reports")
                .data(reportService.getAllGroupReports(groupId, pageable))
                .build();
    }
}
