package com.example.service;

import com.example.model.Admin;
import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
   public User createUser(User user){
        return userRepository.save(user);
   }
   public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));
   }
   public User changeUserProfile (Long id, User user){
        User existingUser = getUserById(id);
        existingUser.setName(user.getName());
        existingUser.setAddress(user.getAddress());
        existingUser.setPhone(user.getPhone());
        return userRepository.save(existingUser);
   }
   public User changeUserPassword(Long id, String password){
        User existingUser = getUserById(id);
        existingUser.setPassword(password);
        return userRepository.save(existingUser);
   }
}
