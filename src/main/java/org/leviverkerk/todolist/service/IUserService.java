package org.leviverkerk.todolist.service;

import org.leviverkerk.todolist.model.User;
import org.leviverkerk.todolist.model.UserDto;

public interface IUserService {
    User registerNewUserAccount(UserDto userDto) throws Exception;
}
