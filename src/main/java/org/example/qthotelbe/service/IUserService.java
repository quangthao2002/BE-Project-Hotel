package org.example.qthotelbe.service;

import org.example.qthotelbe.model.User;

import java.util.List;

public interface IUserService  {

    void registerUser(User user);

    List<User> getUsers();


    User getUserByEmail(String email);

    void deleteUserByEmail(String email);
}
