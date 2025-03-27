package io.github.rtheodoro4201.eventconnect.service;

import io.github.rtheodoro4201.eventconnect.exception.EmailAlreadyExistsException;
import io.github.rtheodoro4201.eventconnect.model.User;
import io.github.rtheodoro4201.eventconnect.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder; // Para criptografar a senha

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String name, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email já cadastrado."); // Crie sua própria exceção
        }
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password)); // Criptografar a senha antes de salvar
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

    public Optional<User> updateUser(Long userId, String newName, String newEmail, String newPassword)
    {
        return userRepository.findById(userId)
                .map(user -> {
                    Optional.ofNullable(newName)
                            .filter(name -> !name.isEmpty())
                            .ifPresent(user::setName);

                    Optional.ofNullable(newEmail)
                            .filter(email -> !email.isEmpty() && !user.getEmail().equals(email))
                            .ifPresent(email -> {
                                if (userRepository.findByEmail(email).isEmpty()) {
                                    user.setEmail(email);
                                } else {
                                    throw new EmailAlreadyExistsException("Email já cadastrado.");
                                }
                            });

                    Optional.ofNullable(newPassword)
                            .filter(password -> !password.isEmpty())
                            .ifPresent(password -> user.setPassword(passwordEncoder.encode(password)));

                    return userRepository.save(user);
                });
    }

    public boolean deleteUser(Long userId)
    {
        if(!userRepository.existsById(userId))
        {
            return false;
        }

        userRepository.deleteById(userId);
        return true;
    }
    // Outros métodos de serviço relacionados a usuários
}
