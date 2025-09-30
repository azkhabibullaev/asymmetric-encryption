package com.example.demo.user.impl;

import com.example.demo.exceptions.BusinessException;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.user.User;
import com.example.demo.user.UserMapper;
import com.example.demo.user.UserRepository;
import com.example.demo.user.UserService;
import com.example.demo.user.request.ChangePasswordRequest;
import com.example.demo.user.request.ProfileUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(final String userEmail) throws UsernameNotFoundException {
        return this.userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User with " + userEmail + " not found"));
    }

    @Override
    public void updateProfileInfo(final ProfileUpdateRequest request, final String userId) {
        final User savedUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));
        this.userMapper.mergeUserInfo(savedUser, request);
        this.userRepository.save(savedUser);
        log.info("User with id {} updated successfully", userId);
    }

    @Override
    public void changePassword(final ChangePasswordRequest request, final String userId) {
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new BusinessException(ErrorCode.CHANGE_PASSWORD_MISMATCH);
        }
        final User savedUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));
        if (!this.passwordEncoder.matches(request.getCurrentPassword(), savedUser.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CURRENT_PASSWORD);
        }
        final String encoded = this.passwordEncoder.encode(request.getNewPassword());
        savedUser.setPassword(encoded);
        this.userRepository.save(savedUser);
        log.info("Change password for user with id {}", userId);
    }

    @Override
    public void deactivateAccount(final String userId) {
        final User savedUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));
        if (!savedUser.isEnabled()) {
            throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_DEACTIVATED, userId);
        }
        savedUser.setEnabled(false);
        this.userRepository.save(savedUser);
        log.info("User {} account has been deactivated", userId);
    }

    @Override
    public void reactivateAccount(final String userId) {
        final User savedUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));
        if (savedUser.isEnabled()) {
            throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_ACTIVE, userId);
        }
        savedUser.setEnabled(true);
        this.userRepository.save(savedUser);
        log.info("User {} account reactivated", userId);
    }

    @Override
    public void deleteAccount(final String userId) {
        final User savedUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));
        if (savedUser.isDeleted()) {
            throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_DELETED, userId);
        }
        savedUser.setDeleted(true);
        savedUser.setEnabled(false);
        this.userRepository.save(savedUser);
        log.info("User {} account soft deleted", userId);
    }

}
