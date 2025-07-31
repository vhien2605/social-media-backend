package com.hien.back_end_app.services;

import com.hien.back_end_app.dto.request.*;
import com.hien.back_end_app.dto.response.user.UserResponseDTO;
import com.hien.back_end_app.entities.Permission;
import com.hien.back_end_app.entities.Role;
import com.hien.back_end_app.entities.User;
import com.hien.back_end_app.exceptions.AppException;
import com.hien.back_end_app.mappers.RoleMapper;
import com.hien.back_end_app.mappers.UserMapper;
import com.hien.back_end_app.repositories.PermissionRepository;
import com.hien.back_end_app.repositories.RoleRepository;
import com.hien.back_end_app.repositories.UserRepository;
import com.hien.back_end_app.utils.enums.AuthProvider;
import com.hien.back_end_app.utils.enums.ErrorCode;
import com.hien.back_end_app.utils.enums.Gender;
import com.hien.back_end_app.utils.enums.UserStatus;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.function.ListaggStringAggEmulation;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final PermissionRepository permissionRepository;


    // just create user with standard login
    public UserResponseDTO create(UserCreationRequestDTO dto) {
        // main fields
        String email = dto.getEmail();
        String password = dto.getPassword();
        String fullName = dto.getFullName();
        var existedUser = userRepository.findByEmailWithNoReferences(email);

        if (existedUser.isPresent()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXIST));

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .fullName(fullName)
                .userStatus(UserStatus.OFFLINE)
                .authProvider(AuthProvider.STANDARD)
                .address(dto.getAddress())
                .dateOfBirth(dto.getDateOfBirth())
                .education(dto.getEducation())
                .work(dto.getWork())
                .gender(dto.getGender())
                .roles(Set.of(userRole))
                .build();
        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    public UserResponseDTO update(UpdateUserInformationRequestDTO dto) {
        long userId = dto.getUserId();
        User user = userRepository.findByIdWithRoles(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setFullName(dto.getFullName());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setAddress(dto.getAddress());
        user.setEducation(dto.getEducation());
        user.setWork(dto.getWork());
        user.setGender(dto.getGender());
        userRepository.save(user);
        return userMapper.toDTO(user);
    }


    @Transactional
    public UserResponseDTO updateRoles(UpdateRolesRequestDTO dto) {
        long userId = dto.getUserId();

        User targetUser = userRepository.findByIdWithRoles(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Set<String> requestedRoleNames = dto.getRoles().stream()
                .map(RoleRequestDTO::getName)
                .collect(Collectors.toSet());
        List<Role> existedRoles = roleRepository.findAllInNames(new ArrayList<>(requestedRoleNames));
        Set<String> existedRoleNames = existedRoles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        List<Role> newRoles = dto.getRoles().stream()
                .filter(r -> !existedRoleNames.contains(r.getName()))
                .map(roleMapper::toEntity)
                .collect(Collectors.toList());
        Set<Role> allRoles = new HashSet<>(existedRoles);
        allRoles.addAll(newRoles);
        Set<String> permissionNamesRequested = new HashSet<>();
        List<Permission> permissionRequests = new ArrayList<>();
        allRoles.forEach(role -> {
            for (Permission p : role.getPermissions()) {
                if (permissionNamesRequested.add(p.getName())) {
                    permissionRequests.add(p);
                }
            }
        });
        List<Permission> existedPermissions = permissionRepository.findAllInNames(new ArrayList<>(permissionNamesRequested));
        Set<String> existedPermissionNames = existedPermissions.stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());
        List<Permission> newPermissions = permissionRequests.stream()
                .filter(p -> !existedPermissionNames.contains(p.getName()))
                .collect(Collectors.toList());
        targetUser.setRoles(allRoles);
        permissionRepository.saveAll(newPermissions);
        roleRepository.saveAll(newRoles);
        userRepository.save(targetUser);
        return userMapper.toDTO(targetUser);
    }

}
