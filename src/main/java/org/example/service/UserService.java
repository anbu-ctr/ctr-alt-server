package org.example.service;

import org.example.dto.SignInRequest;
import org.example.dto.SignInResponse;
import org.example.dto.SignUpRequest;
import org.example.dto.UserDto;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Sign Up - Create new user with hashed password
     */
    public SignInResponse signUp(final SignUpRequest request) {
        if (request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty()) {
            return new SignInResponse(false, "PhoneNumber is required");
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            return new SignInResponse(false, "Password is required");
        }

        if (!isValidPhoneNumber(request.getPhoneNumber())) {
            return new SignInResponse(false, "Invalid phoneNumber format");
        }

        if (request.getPassword().length() < 6) {
            return new SignInResponse(false, "Password must be at least 6 characters");
        }

        if (!Objects.equals(request.getConfirmPassword(), request.getPassword())) {
            return new SignInResponse(false, "Password mismatch");
        }
        final Optional<User> existingUser = userRepository.findByPhoneNumber(request.getPhoneNumber());

        if (existingUser.isPresent()) {
            return new SignInResponse(false, "phoneNumber already registered");
        }
        final User user = new User();

        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setOrgName(request.getOrgName());

        final User savedUser = userRepository.save(user);
        final String token = jwtUtil.generateToken(savedUser.getPhoneNumber(), Long.valueOf(savedUser.getId()));
        final UserDto userDTO = new UserDto(
                savedUser.getId(),
                savedUser.getPhoneNumber(),
                savedUser.getCreatedAt().toString(),
               savedUser.getName()
        );

        return new SignInResponse(true, "Sign up successful", token, userDTO);
    }

    /**
     * Sign In - Authenticate user with hashed password and return JWT
     */
    public SignInResponse signIn(final SignInRequest request) {

        if (request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty()) {
            return new SignInResponse(false, "Email is required");
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            return new SignInResponse(false, "Password is required");
        }
        final Optional<User> userOptional = userRepository.findByPhoneNumber(request.getPhoneNumber());

        if (userOptional.isEmpty()) {
            return new SignInResponse(false, "Invalid email or password");
        }
        final User user = userOptional.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new SignInResponse(false, "Invalid email or password");
        }
        final String token = jwtUtil.generateToken(user.getPhoneNumber(), Long.valueOf(user.getId()));
        final UserDto userDTO = new UserDto(
                user.getId(),
                user.getPhoneNumber(),
                user.getCreatedAt().toString(),
                user.getName()
        );

        return new SignInResponse(true, "Sign in successful", token, userDTO);
    }

    /**
     * Validate email format
     */
    private boolean isValidPhoneNumber(final String phoneNumber) {
       final String phoneRegex = "^[6-9][0-9]{9}$";
        return phoneNumber.matches(phoneRegex);
    }
}