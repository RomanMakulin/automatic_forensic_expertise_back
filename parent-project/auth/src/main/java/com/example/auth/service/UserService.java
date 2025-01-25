package com.example.auth.service;


import com.example.auth.model.User;

public interface UserService {

    User createUser(String fullName, String email, String password);

}
