package com.example.service;

import com.example.model.Admin;
import com.example.model.User;
import com.example.repository.AdminRepository;
import com.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    public AdminService(AdminRepository adminRepository, UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
    }

    public Admin createAdmin(Admin admin){
        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        return adminRepository.save(admin);
    }

    public Admin getAdminById(Long id) {
        return adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));
    }

    public Admin deactivateAdmin(Long id) {
        Admin admin = getAdminById(id);
        admin.setIsActive(false);
        return adminRepository.save(admin);
    }

    public Admin assignRole(Long id, String role) {
        Admin admin = getAdminById(id);
        admin.setRole(role);
        return adminRepository.save(admin);
    }
    public Admin makeUserToAdmin(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Admin newAdmin = convertUserToAdmin(existingUser);

        return adminRepository.save(newAdmin);
    }

    private Admin convertUserToAdmin(User user) {
        Admin admin = new Admin();
        admin.setName(user.getName());
        admin.setEmail(user.getEmail());
        admin.setAddress(user.getAddress());
        admin.setPhone(user.getPhone());
        admin.setPassword(user.getPassword());
        admin.setRole("ADMIN");
        admin.setIsActive(true);
        return admin;
    }
    public Admin changeAdminProfile(Long id, Admin admin) {
        Admin existingAdmin = getAdminById(id);
        existingAdmin.setName(admin.getName());
        existingAdmin.setAddress(admin.getAddress());
        existingAdmin.setPhone(admin.getPhone());
        return adminRepository.save(existingAdmin);
    }
    public Admin changeAdminPassword(Long id, String password) {
        Admin existingAdmin = getAdminById(id);
        existingAdmin.setPassword(password);
        return adminRepository.save(existingAdmin);
    }


}
