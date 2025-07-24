package com.hien.back_end_app.services;


import com.hien.back_end_app.dto.request.CreateGroupRequestDTO;
import com.hien.back_end_app.dto.response.group.GroupResponseDTO;
import com.hien.back_end_app.dto.response.user.UserResponseDTO;
import com.hien.back_end_app.entities.Group;
import com.hien.back_end_app.entities.GroupUser;
import com.hien.back_end_app.entities.Role;
import com.hien.back_end_app.entities.User;
import com.hien.back_end_app.exceptions.AppException;
import com.hien.back_end_app.mappers.GroupMapper;
import com.hien.back_end_app.repositories.GroupRepository;
import com.hien.back_end_app.repositories.GroupUserRepository;
import com.hien.back_end_app.repositories.RoleRepository;
import com.hien.back_end_app.repositories.UserRepository;
import com.hien.back_end_app.utils.commons.GlobalMethod;
import com.hien.back_end_app.utils.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final RoleRepository roleRepository;
    private final GroupUserRepository groupUserRepository;
    private final GroupMapper groupMapper;


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
}
