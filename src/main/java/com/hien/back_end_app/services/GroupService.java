package com.hien.back_end_app.services;


import com.hien.back_end_app.dto.request.CreateGroupRequestDTO;
import com.hien.back_end_app.dto.response.PageResponseDTO;
import com.hien.back_end_app.dto.response.group.GroupResponseDTO;
import com.hien.back_end_app.dto.response.group.JoinGroupRequestResponseDTO;
import com.hien.back_end_app.dto.response.group.PostRequestResponseDTO;
import com.hien.back_end_app.dto.response.post.PostResponseDTO;
import com.hien.back_end_app.entities.*;
import com.hien.back_end_app.exceptions.AppException;
import com.hien.back_end_app.mappers.GroupMapper;
import com.hien.back_end_app.mappers.JoinGroupRequestMapper;
import com.hien.back_end_app.mappers.PostMapper;
import com.hien.back_end_app.mappers.PostRequestMapper;
import com.hien.back_end_app.repositories.*;
import com.hien.back_end_app.utils.commons.GlobalMethod;
import com.hien.back_end_app.utils.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final RoleRepository roleRepository;
    private final GroupUserRepository groupUserRepository;
    private final GroupMapper groupMapper;
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UploadPostRequestRepository uploadPostRequestRepository;
    private final PostRequestMapper postRequestMapper;
    private final JoinGroupRequestRepository joinGroupRequestRepository;
    private final JoinGroupRequestMapper joinGroupRequestMapper;


    @Transactional
    public GroupResponseDTO createGroup(CreateGroupRequestDTO dto) {
        String name = dto.getName();
        String description = dto.getDescription();

        String email = GlobalMethod.extractEmailFromContext();
        User createdUser = userRepository.findByEmailWithNoReferences(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Role role = roleRepository.findByName("GROUP_ADMIN")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXIST));

        Group group = Group.builder()
                .createdBy(createdUser)
                .name(name)
                .description(description)
                .build();
        groupRepository.save(group);
        GroupUser groupUser = GroupUser.builder()
                .group(group)
                .user(createdUser)
                .role(role)
                .build();
        groupUserRepository.save(groupUser);
        return groupMapper.toDTO(group);
    }

    public PageResponseDTO<Object> groupPosts(Long groupId, Pageable pageable) {
        Page<Post> posts = postRepository.findGroupPosts(groupId, pageable);
        List<Long> postIds = posts.stream().map(Post::getId).toList();
        List<Post> fetchedPosts = postRepository.findPostsWithPostMediaAndEmotions(postIds);
        Map<Long, Post> idPostMap = fetchedPosts.stream().collect(Collectors.toMap(
                Post::getId,
                p -> p
        ));
        List<PostResponseDTO> dtos = postIds.stream()
                .map(idPostMap::get)
                .map(postMapper::toDTO)
                .toList();
        return PageResponseDTO.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(posts.getTotalPages())
                .data(dtos)
                .build();
    }

    public PageResponseDTO<Object> requestPosts(Long groupId, Pageable pageable) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException(ErrorCode.GROUP_NOT_EXIST));
        User ownedGroupUser = group.getCreatedBy();
        String email = GlobalMethod.extractEmailFromContext();
        User adminUser = userRepository.findByEmailWithNoReferences(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (adminUser.getId() != ownedGroupUser.getId()) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        Page<UploadPostRequest> uploadPostRequests = uploadPostRequestRepository.findUploadsByGroupId(groupId, pageable);
        List<Long> ids = uploadPostRequests.stream()
                .map(UploadPostRequest::getId)
                .toList();
        List<UploadPostRequest> fetchedRequests = uploadPostRequestRepository.findUploadsWithMediasByIds(ids);
        Map<Long, UploadPostRequest> idRequestMap = fetchedRequests
                .stream().collect(Collectors.toMap(UploadPostRequest::getId, g -> g));

        List<PostRequestResponseDTO> dtos = ids.stream()
                .map(idRequestMap::get)
                .map(postRequestMapper::toDTO)
                .toList();
        return PageResponseDTO.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(uploadPostRequests.getTotalPages())
                .data(dtos)
                .build();
    }

    public PageResponseDTO<Object> joinRequests(Long groupId, Pageable pageable) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException(ErrorCode.GROUP_NOT_EXIST));
        User ownedGroupUser = group.getCreatedBy();
        String email = GlobalMethod.extractEmailFromContext();
        User adminUser = userRepository.findByEmailWithNoReferences(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (adminUser.getId() != ownedGroupUser.getId()) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        Page<JoinGroupRequest> joinGroupRequests = joinGroupRequestRepository.findByGroupId(groupId, pageable);
        List<JoinGroupRequestResponseDTO> dtos = joinGroupRequests.stream().
                map(joinGroupRequestMapper::toDTO)
                .toList();

        return PageResponseDTO.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(joinGroupRequests.getTotalPages())
                .data(dtos)
                .build();
    }
}
