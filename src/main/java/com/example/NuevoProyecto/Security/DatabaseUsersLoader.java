package com.example.NuevoProyecto.Security;


import com.example.NuevoProyecto.User.User;
import com.example.NuevoProyecto.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseUsersLoader {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostConstruct
    private void initDatabase() {
        List<String> adminRoles = new ArrayList<>();
        adminRoles.add("ADMIN");
        adminRoles.add("USER");
        userRepository.save(new User("admin", passwordEncoder.encode("adminpass"),"adminLastName", adminRoles));
    }
}