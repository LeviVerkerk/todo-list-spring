package org.leviverkerk.todolist.service;

import org.leviverkerk.todolist.model.User;
import org.leviverkerk.todolist.model.UserDto;
import org.leviverkerk.todolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;

@Service
public class UserService implements IUserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Override
    public User registerNewUserAccount(UserDto userDto) throws Exception {
        if (emailExist(userDto.getEmail())) {
            throw new Exception("There is an account with that email address: "
                    + userDto.getEmail());
        }

        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setEnabled(true);
        user.setRoles(Collections.singletonList("USER"));

        return userRepository.save(user);
    }
    private boolean emailExist(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentEmail;
        if (principal instanceof org.springframework.security.core.userdetails.User){
            currentEmail = ((org.springframework.security.core.userdetails.User) principal).getUsername();
        } else {
            currentEmail = principal.toString();
        }

        return userRepository.findByEmail(currentEmail);
    }
}
