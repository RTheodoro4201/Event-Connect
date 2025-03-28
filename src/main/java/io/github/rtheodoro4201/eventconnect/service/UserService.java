package io.github.rtheodoro4201.eventconnect.service;

import io.github.rtheodoro4201.eventconnect.exception.EmailAlreadyExistsException;
import io.github.rtheodoro4201.eventconnect.model.User;
import io.github.rtheodoro4201.eventconnect.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String name, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already registered.");
        }
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        return userRepository.save(newUser);
    }

    public Optional<User> loginUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> updateUser(Long userId, String newName, String newEmail, String newPassword) {
        return userRepository.findById(userId)
                .map(updatedUser -> {
                    Optional.ofNullable(newName)
                            .filter(name -> !name.isEmpty())
                            .ifPresent(updatedUser::setName);

                    Optional.ofNullable(newEmail)
                            .filter(email -> !email.isEmpty() && !updatedUser.getEmail().equals(email))
                            .ifPresent(email -> {
                                if (userRepository.findByEmail(email).isEmpty()) {
                                    updatedUser.setEmail(email);
                                } else {
                                    throw new EmailAlreadyExistsException("Email already registered.");
                                }
                            });

                    Optional.ofNullable(newPassword)
                            .filter(password -> !password.isEmpty())
                            .ifPresent(password -> updatedUser.setPassword(passwordEncoder.encode(password)));

                    return userRepository.save(updatedUser);
                });
    }

    private void checkUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }
    }

    public boolean deleteUser(Long userId) {
        checkUser(userId);

        userRepository.deleteById(userId);
        return true;
    }
}
