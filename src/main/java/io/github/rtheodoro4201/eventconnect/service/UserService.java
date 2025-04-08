package io.github.rtheodoro4201.eventconnect.service;

import io.github.rtheodoro4201.eventconnect.dto.user.*;
import io.github.rtheodoro4201.eventconnect.exception.EmailAlreadyExistsException;
import io.github.rtheodoro4201.eventconnect.model.User;
import io.github.rtheodoro4201.eventconnect.repository.UserRepository;
import io.github.rtheodoro4201.eventconnect.security.ApplicationUserDetailsService;
import io.github.rtheodoro4201.eventconnect.security.JwtUtil;
import io.github.rtheodoro4201.eventconnect.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityUtils entityUtils;
    private final AuthenticationManager authenticationManager;
    private final ApplicationUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EntityUtils entityUtils, AuthenticationManager authenticationManager, ApplicationUserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.entityUtils = entityUtils;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    public User registerUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered.");
        }
        User newUser = new User();
        newUser.setName(registrationDTO.getName());
        newUser.setEmail(registrationDTO.getEmail());
        newUser.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        return userRepository.save(newUser);
    }

    public String loginUser(UserLoginDTO loginDTO) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getEmail());
            userDetails.getPassword();
            return jwtUtil.generateToken(userDetails);
        } catch (Exception e) {
            logger.error("Error during login or token generation:", e);
            return null;
        }
    }

    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> updateUser(UserUpdateDTO updateDTO, Long userId) {
        return userRepository.findById(userId)
                .map(updatedUser -> {
                    Optional.ofNullable(updateDTO.getName())
                            .filter(name -> !name.isEmpty())
                            .ifPresent(updatedUser::setName);

                    Optional.ofNullable(updateDTO.getEmail())
                            .filter(email -> !email.isEmpty() && !updatedUser.getEmail().equals(email))
                            .ifPresent(email -> {
                                if (userRepository.findByEmail(email).isEmpty()) {
                                    updatedUser.setEmail(email);
                                } else {
                                    throw new EmailAlreadyExistsException("Email already registered.");
                                }
                            });

                    Optional.ofNullable(updateDTO.getPassword())
                            .filter(password -> !password.isEmpty())
                            .ifPresent(password -> updatedUser.setPassword(passwordEncoder.encode(password)));

                    return userRepository.save(updatedUser);
                });
    }

    public void deleteUser(Long userId) {
        entityUtils.checkEntity(userId, User.class);
        userRepository.deleteById(userId);
    }

    public Optional<User> findUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail);
    }
}
