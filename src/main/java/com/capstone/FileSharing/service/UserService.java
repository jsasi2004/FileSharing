package com.capstone.FileSharing.service;

import com.capstone.FileSharing.model.Role;
import com.capstone.FileSharing.model.User;
import com.capstone.FileSharing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolesService rolesService;

    // @PostConstruct
    // public void init() {
    //     userRepository.save(
    //         new User("Admin", "admin@gmail.com", passwordEncoder.encode("admin"), rolesService.getAdminRole())
    //     );
    // }

    // Register new user
    public User register(User user)  throws Exception {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new Exception("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new Exception("Email already exists");
        }

        Role userRole = rolesService.getUserRole();
        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(userRole);

        userRole.getUsers().add(user);

        return userRepository.save(user);
    }

    // Authenticate user
    public boolean authenticate(String username, String rawPassword) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Check password
            return passwordEncoder.matches(rawPassword, user.getPassword());
        }
        return false;
    }

    // Optional: find user by username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
