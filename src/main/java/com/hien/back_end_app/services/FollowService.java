package com.hien.back_end_app.services;


import com.hien.back_end_app.entities.User;
import com.hien.back_end_app.exceptions.AppException;
import com.hien.back_end_app.repositories.FollowRepository;
import com.hien.back_end_app.repositories.UserRepository;
import com.hien.back_end_app.utils.commons.GlobalMethod;
import com.hien.back_end_app.utils.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public String unfollow(Long userId) {
        String email = GlobalMethod.extractEmailFromContext();
        User followUser = userRepository.findByEmailWithNoReferences(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        followRepository.deleteByUserTargetIdAndUserFollowId(userId, followUser.getId());
        return "unfollowed " + targetUser.getFullName();
    }
}
