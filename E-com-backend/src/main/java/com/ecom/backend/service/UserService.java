package com.ecom.backend.service;
import com.ecom.backend.exception.NotFoundException;
import com.ecom.backend.model.User;
import com.ecom.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserByName(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUser(String userName, User updatedUser) {
        User existingUser = getUserByName(userName);
        existingUser.setUserName(updatedUser.getUserName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setAddress(updatedUser.getAddress());
        return userRepository.save(existingUser);
    }

    public void deleteUser(String userName) {
        userRepository.deleteByUserName(userName);
    }

    public User getUserById(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    }
}
