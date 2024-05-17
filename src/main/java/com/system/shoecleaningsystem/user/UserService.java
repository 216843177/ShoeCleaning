package com.system.shoecleaningsystem.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;

    public void saveUser(User user){
        userRepository.save(user);
    }

    public User getByUsernameAndPassword(String username, String password){
        User user = userRepository.findByUsernameAndPassword(username, password);

        return user;
    }
}
