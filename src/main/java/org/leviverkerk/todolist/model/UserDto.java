package org.leviverkerk.todolist.model;

import lombok.Data;
import org.leviverkerk.todolist.util.PasswordMatches;
import org.leviverkerk.todolist.util.ValidEmail;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;

@Data
@PasswordMatches
public class UserDto {
    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;

    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    private String password;
    private String matchingPassword;

    @ValidEmail
    @NotNull
    @NotEmpty
    private String email;

}
