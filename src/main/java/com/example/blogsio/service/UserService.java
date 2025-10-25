package com.example.blogsio.service;

import com.example.blogsio.dto.PasswordChangeDto;
import com.example.blogsio.dto.ResetPasswordDto;
import com.example.blogsio.dto.UserDetailDto;
import com.example.blogsio.dto.UserProfileDto;
import com.example.blogsio.entity.PasswordResetToken;
import com.example.blogsio.entity.UserEntity;
import com.example.blogsio.enums.userRole;
import com.example.blogsio.repository.PasswordResetTokenRepository;
import com.example.blogsio.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PasswordResetTokenRepository tokenRepository; // Inject new repository
    @Autowired
    private JavaMailSender mailSender;

    public UserEntity createUser(UserEntity user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepository.save(user);
    }

    public UserEntity updateUser(long id, UserEntity user) {
        if (userRepository.existsById(id)) {
            UserEntity current = userRepository.findById(id).get();
            current.setEmail(user.getEmail());
            current.setRole(user.getRole());
            if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
                current.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
            }
            return userRepository.save(current);
        }
        return null;
    }
    @Transactional
    public boolean deleteUserByID(long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    public UserEntity getByID(long id) {
        return userRepository.findById(id).orElse(null);
    }
    public Page<UserDetailDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::convertEntityToDto);
    }

    public UserDetailDto updateUserRole(Long userId, userRole role) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(role);
        UserEntity updatedUser = userRepository.save(user);
        return convertEntityToDto(updatedUser);
    }
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(userId);
    }

    // Helper method to convert UserEntity to UserDetailDto
    private UserDetailDto convertEntityToDto(UserEntity user) {
        UserDetailDto dto = new UserDetailDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
    @Transactional(readOnly = true)
    public UserDetailDto getUserProfile(Principal principal) {
        UserEntity user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return convertEntityToDto(user);
    }

    @Transactional
    public UserDetailDto updateUserProfile(Principal principal, UserProfileDto profileDto) {
        UserEntity user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setName(profileDto.getName());
        UserEntity updatedUser = userRepository.save(user);
        return convertEntityToDto(updatedUser);
    }

    @Transactional
    public void changePassword(Principal principal, PasswordChangeDto passwordDto) {
        UserEntity user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 1. Check if the old password is correct
        if (!passwordEncoder.matches(passwordDto.getOldPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid old password");
        }

        // 2. Encode and set the new password
        user.setPasswordHash(passwordEncoder.encode(passwordDto.getNewPassword()));
        userRepository.save(user);
    }
    @Transactional
    public void createPasswordResetTokenForUser(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        String token = UUID.randomUUID().toString();
        PasswordResetToken myToken = new PasswordResetToken();
        myToken.setToken(token);
        myToken.setUser(user);
        myToken.setExpiryDate(LocalDateTime.now().plusMinutes(15)); // 15 minute expiry
        tokenRepository.save(myToken);

        // Send the email
        String recipientAddress = user.getEmail();
        String subject = "Password Reset Request";
        String confirmationUrl = "https://main.d1zc7i69uhajyw.amplifyapp.com/reset-password?token=" + token; // Your frontend URL
        String message = "Hi blogger! \n Please click the link below to reset your password.\nThis link will expire in 15 minutes.";

        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(recipientAddress);
        emailMessage.setSubject(subject);
        emailMessage.setText(message + "\r\n" + confirmationUrl);
        mailSender.send(emailMessage);
    }

    @Transactional
    public void resetPassword(ResetPasswordDto resetPasswordDto) {
        PasswordResetToken passToken = tokenRepository.findByToken(resetPasswordDto.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid password reset token"));

        if (passToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Expired password reset token");
        }

        UserEntity user = passToken.getUser();
        user.setPasswordHash(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
        userRepository.save(user);

        // Delete the token so it cannot be used again
        tokenRepository.delete(passToken);
    }
}