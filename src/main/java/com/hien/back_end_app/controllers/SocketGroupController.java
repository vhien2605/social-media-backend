package com.hien.back_end_app.controllers;


import com.hien.back_end_app.dto.request.AddMemberRequestDTO;
import com.hien.back_end_app.dto.request.CheckJoinRequestDTO;
import com.hien.back_end_app.dto.request.CheckPostRequestDTO;
import com.hien.back_end_app.dto.request.JoinGroupRequestDTO;
import com.hien.back_end_app.services.SocketGroupService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
public class SocketGroupController {
    private final SocketGroupService socketGroupService;

    @MessageMapping("/group/add-member")
    public void addMemberToGroup(@Valid AddMemberRequestDTO dto, SimpMessageHeaderAccessor accessor) {
        socketGroupService.addMember(dto, accessor);
    }

    @MessageMapping("/group/member-join")
    public void sendRequestJoinGroup(@Valid JoinGroupRequestDTO dto, SimpMessageHeaderAccessor accessor) {
        socketGroupService.createJoinRequest(dto, accessor);
    }

    @MessageMapping("/group/check-join-request/{joinRequestId}")
    public void acceptedJoinRequest(@DestinationVariable @Min(value = 0, message = "joinRequestId must not negative") Long joinRequestId
            , @Valid CheckJoinRequestDTO dto
            , SimpMessageHeaderAccessor accessor) {
        socketGroupService.checkJoinRequest(joinRequestId, dto, accessor);
    }

    @MessageMapping("/group/check-post-request/{postRequest}")
    public void acceptedPostRequest(@DestinationVariable @Min(value = 0, message = "postRequest id must not be negative") Long postRequest
            , @Valid CheckPostRequestDTO dto
            , SimpMessageHeaderAccessor accessor
    ) {
        socketGroupService.checkPostRequest(postRequest, dto, accessor);
    }
}
