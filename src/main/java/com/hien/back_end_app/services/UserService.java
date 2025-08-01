package com.hien.back_end_app.services;

import com.hien.back_end_app.dto.request.*;
import com.hien.back_end_app.dto.response.ApiResponse;
import com.hien.back_end_app.dto.response.PageResponseDTO;
import com.hien.back_end_app.dto.response.user.UserDetailResponseDTO;
import com.hien.back_end_app.dto.response.user.UserResponseDTO;
import com.hien.back_end_app.entities.Message;
import com.hien.back_end_app.entities.Permission;
import com.hien.back_end_app.entities.Role;
import com.hien.back_end_app.entities.User;
import com.hien.back_end_app.exceptions.AppException;
import com.hien.back_end_app.mappers.RoleMapper;
import com.hien.back_end_app.mappers.UserMapper;
import com.hien.back_end_app.repositories.PermissionRepository;
import com.hien.back_end_app.repositories.RoleRepository;
import com.hien.back_end_app.repositories.UserRepository;
import com.hien.back_end_app.repositories.specification.SpecificationBuilder;
import com.hien.back_end_app.utils.commons.AppConst;
import com.hien.back_end_app.utils.enums.AuthProvider;
import com.hien.back_end_app.utils.enums.ErrorCode;
import com.hien.back_end_app.utils.enums.Gender;
import com.hien.back_end_app.utils.enums.UserStatus;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.function.ListaggStringAggEmulation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    public PageResponseDTO<Object> getUsersPagination(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        List<Long> userIds = users.stream().map(User::getId).toList();
        List<User> fetchedUsers = userRepository.findAllByIds(userIds);
        Map<Long, User> idUserMap = fetchedUsers.stream()
                .collect(Collectors.toMap(User::getId, fu -> fu));
        List<UserResponseDTO> dtos = userIds.stream()
                .map(idUserMap::get)
                .map(userMapper::toDTO)
                .toList();

        return PageResponseDTO.builder()
                .data(dtos)
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(users.getTotalPages())
                .build();
    }


    public PageResponseDTO<Object> getUsersFilter(Pageable pageable, String[] user, String[] sortBy) {
        SpecificationBuilder<User> builder = new SpecificationBuilder<>();
        Pattern pattern = Pattern.compile(AppConst.SEARCH_SPEC_OPERATOR);
        Pattern sortPattern = Pattern.compile(AppConst.SORT_BY);
        // loop conversation and build specification
        // loop other predicate
        for (String s : user) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3),
                        matcher.group(4), matcher.group(5), matcher.group(6));
            }
        }
        Specification<User> specification = builder.build();

        // add sort by createAt desc
        List<Sort.Order> sortOrders = new ArrayList<>();
        for (String sb : sortBy) {
            Matcher sortMatcher = sortPattern.matcher(sb);
            if (sortMatcher.find()) {
                String field = sortMatcher.group(1);
                String value = sortMatcher.group(3);
                Sort.Direction direction = (value.equalsIgnoreCase("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC;
                sortOrders.add(new Sort.Order(direction, field));
            }
        }
        Sort sort = Sort.by(sortOrders);
        // insert sort property into pageable
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                sort);
        Page<User> users = userRepository.findAll(specification, sortedPageable);

        List<Long> userIds = users.stream().map(User::getId).toList();
        List<User> fetchedUsers = userRepository.findAllByIds(userIds);
        Map<Long, User> idUserMap = fetchedUsers.stream()
                .collect(Collectors.toMap(User::getId, fu -> fu));
        List<UserResponseDTO> dtos = userIds.stream()
                .map(idUserMap::get)
                .map(userMapper::toDTO)
                .toList();

        return PageResponseDTO.builder()
                .data(dtos)
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(users.getTotalPages())
                .build();
    }

    public UserDetailResponseDTO getDetailInformation(Long userId) {
        User user = userRepository.findByIdWithFollowersAndAlbums(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserDetailDTO(user);
    }
}
