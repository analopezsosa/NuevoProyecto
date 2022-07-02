package com.example.NuevoProyecto.User;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;


    public void addUser(User username){
        userRepository.save(username);

    }

    public User removeUser(String username){
        User usertodelete= this.getUser(username);
        if(usertodelete!=null){
            userRepository.delete(usertodelete);
        }
        return usertodelete;

    }
    public Collection<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUser(String username)   {
        Optional<User> present = userRepository.findById(username);

        return present.orElse(null);
    }
    public User updateUser(String username,User user){
        user.setUser(username);
        return userRepository.save(user);
    }



}