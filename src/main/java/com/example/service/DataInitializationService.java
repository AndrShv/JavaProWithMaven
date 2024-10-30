package com.example.service;


import com.example.model.Admin;
import com.example.model.User;
import com.example.repository.AdminRepository;
import com.example.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class DataInitializationService {

   private final UserRepository userRepository;
   private final AdminRepository adminRepository;


    public DataInitializationService(UserRepository userRepository, AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }
    @Transactional
    @PostConstruct
    public void init() {
        createAdmins();
        createUsers();
    }

    private void createAdmins() {
        try{
        Admin admin1 = new Admin();
        admin1.setName("Alice Johnson");
        admin1.setEmail("alice.johnson@admin.com");
        admin1.setAddress("789 Elm St");
        admin1.setPhone("1122334455");
        admin1.setPassword("adminPass1");
        admin1.setIsActive(true);
        admin1.setRole("ADMIN");

        Admin admin2 = new Admin();
        admin2.setName("Bob Brown");
        admin2.setEmail("bob.brown@admin.com");
        admin2.setAddress("321 Oak St");
        admin2.setPhone("6677889900");
        admin2.setPassword("adminPass2");
        admin2.setIsActive(false);
        admin2.setRole("ADMIN");

        adminRepository.save(admin1);
        adminRepository.save(admin2);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void createUsers() {
        try{
        User user1 = new User();
        user1.setName("John Doe");
        user1.setEmail("john.doe@example.com");
        user1.setAddress("123 Main St");
        user1.setPhone("1234567890");
        user1.setPassword("password123");
        user1.setIsActive(true);
        user1.setRole("USER");

        User user2 = new User();
        user2.setName("Jane Smith");
        user2.setEmail("jane.smith@example.com");
        user2.setAddress("456 Park Ave");
        user2.setPhone("0987654321");
        user2.setPassword("securePassword");
        user2.setIsActive(false);
        user2.setRole("USER");

        User user3 = new User();
        user3.setName("Michael Brown");
        user3.setEmail("michael.brown@example.com");
        user3.setAddress("852 Maple Rd");
        user3.setPhone("5555555555");
        user3.setPassword("michaelPassword");
        user3.setIsActive(true);
        user3.setRole("USER");

        User user4 = new User();
        user4.setName("Emma Wilson");
        user4.setEmail("emma.wilson@example.com");
        user4.setAddress("963 Pine St");
        user4.setPhone("6666666666");
        user4.setPassword("emmaPassword");
        user4.setIsActive(true);
        user4.setRole("USER");

        User user5 = new User();
        user5.setName("Olivia Garcia");
        user5.setEmail("olivia.garcia@example.com");
        user5.setAddress("147 Cherry Ln");
        user5.setPhone("7777777777");
        user5.setPassword("oliviaPassword");
        user5.setIsActive(false);
        user5.setRole("USER");

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
