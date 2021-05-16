package org.leviverkerk.todolist.service;

import org.leviverkerk.todolist.model.User;
import org.leviverkerk.todolist.model.UserDto;
import org.leviverkerk.todolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerNewUserAccount(UserDto userDto) throws Exception {
        if (emailExist(userDto.getEmail())) {
            throw new Exception("There is an account with that email address: "
                    + userDto.getEmail());
        }

        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());

        return userRepository.save(user);
    }
    private boolean emailExist(String email) {
        return userRepository.findByEmail(email) != null;
    }
}
