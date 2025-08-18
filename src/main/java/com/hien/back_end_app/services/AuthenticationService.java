package com.hien.back_end_app.services;


import com.hien.back_end_app.dto.request.*;
import com.hien.back_end_app.dto.response.auth.JwtResponseDTO;
import com.hien.back_end_app.dto.response.auth.ResetResponseDTO;
import com.hien.back_end_app.dto.response.user.UserResponseDTO;
import com.hien.back_end_app.entities.RedisToken;
import com.hien.back_end_app.entities.Role;
import com.hien.back_end_app.entities.Token;
import com.hien.back_end_app.entities.User;
import com.hien.back_end_app.exceptions.AppException;
import com.hien.back_end_app.mappers.UserMapper;
import com.hien.back_end_app.repositories.RoleRepository;
import com.hien.back_end_app.repositories.UserRepository;
import com.hien.back_end_app.utils.enums.AuthProvider;
import com.hien.back_end_app.utils.enums.ErrorCode;
import com.hien.back_end_app.utils.enums.MailTemplateType;
import com.hien.back_end_app.utils.enums.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RedisTokenService redisTokenService;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final EmailService emailService;


    public JwtResponseDTO login(LoginStandardRequestDTO dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String userPassword = user.getPassword();
        if (!passwordEncoder.matches(password, userPassword)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // gen token
        String accessToken = jwtService.generateToken(user, TokenType.ACCESS);
        String refreshToken = jwtService.generateToken(user, TokenType.REFRESH);

        // save refresh token in persistent db
        Token dbRefreshToken = Token.builder()
                .jti(jwtService.extractJti(refreshToken))
                .email(user.getEmail())
                .build();
        tokenService.saveToken(dbRefreshToken);

        return JwtResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String logout(LogoutRequestDTO dto) {
        String accessToken = dto.getAccessToken();
        String refreshToken = dto.getRefreshToken();
        jwtService.checkValid(accessToken, TokenType.ACCESS);
        jwtService.checkValid(refreshToken, TokenType.REFRESH);
        // if oke, logout-> black list access, disable refresh
        redisTokenService.saveToken(buildRedisToken(accessToken));
        tokenService.deleteById(jwtService.extractJti(refreshToken));
        return "Logout successfully";
    }


    private RedisToken buildRedisToken(String token) {
        // set disable time until expire
        String jti = jwtService.extractJti(token);
        String email = jwtService.extractUsername(token);
        long timeToLive = (jwtService.extractExpiration(token).getTime() - new Date().getTime()) / 1000;
        return RedisToken.builder()
                .email(email)
                .jti(jti)
                .timeToLive(timeToLive)
                .build();
    }

    public UserResponseDTO register(RegisterRequestDTO dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();
        boolean existsByEmail = userRepository.existsByEmail(email);
        if (existsByEmail) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(email);
        user.setAuthProvider(AuthProvider.STANDARD);
        user.setPassword(passwordEncoder.encode(password));
        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXIST));
        user.setRoles(Set.of(role));
        userRepository.save(user);
        // send mail thank you
        emailService.sendMessage("hvu6582@gmail.com", email, MailTemplateType.ACCOUNT_REGISTRATION, null);
        return userMapper.toDTO(user);
    }

    public JwtResponseDTO refresh(RefreshRequestDTO dto) {
        String refreshToken = dto.getRefreshToken();
        jwtService.checkValid(refreshToken, TokenType.REFRESH);
        String email = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String newAccess = jwtService.generateToken(user, TokenType.ACCESS);
        String newRefresh = jwtService.generateToken(user, TokenType.REFRESH);

        return JwtResponseDTO.builder()
                .accessToken(newAccess)
                .refreshToken(newRefresh).
                build();
    }


    public String changePassword(ChangePasswordRequestDTO dto) {
        String accessToken = dto.getAccessToken();
        String newPassword = dto.getNewPassword();
        jwtService.checkValid(accessToken, TokenType.ACCESS);
        String email = jwtService.extractUsername(accessToken);
        User user = userRepository.findByEmailWithNoReferences(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!user.getAuthProvider().equals(AuthProvider.STANDARD)) {
            throw new AppException(ErrorCode.OAuth2InvalidProvider);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        emailService.sendMessage("hvu6582@gmail.com", email, MailTemplateType.PASSWORD_RESET, null);
        return "Changed new password";
    }

    public ResetResponseDTO forgot(ForgotPasswordRequestDTO dto) {
        String registerEmail = dto.getRegisterEmail();
        // send resetToken via email
        // but for simply, send to rest
        User user = userRepository.findByEmail(registerEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!user.getAuthProvider().equals(AuthProvider.STANDARD)) {
            throw new AppException(ErrorCode.OAuth2InvalidProvider);
        }

        String resetToken = jwtService.generateToken(user, TokenType.RESET);

        return ResetResponseDTO.builder()
                .resetToken(resetToken)
                .build();
    }

    public String reset(ResetPasswordRequestDTO dto) {
        String resetToken = dto.getResetToken();
        String resetPassword = dto.getResetPassword();
        jwtService.checkValid(resetToken, TokenType.RESET);
        String email = jwtService.extractUsername(resetToken);
        boolean isUserExist = userRepository.existsByEmail(email);
        if (isUserExist) {
            userRepository.updatePasswordByEmail(email, passwordEncoder.encode(resetPassword));
            emailService.sendMessage("hvu6582@gmail.com", email, MailTemplateType.PASSWORD_RESET, null);
        }
        return "reset password";
    }
}

