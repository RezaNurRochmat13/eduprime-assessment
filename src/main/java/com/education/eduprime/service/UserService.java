package com.education.eduprime.service;

import com.education.eduprime.model.User;
import com.education.eduprime.model.dto.DetailUserDto;
import com.education.eduprime.model.dto.ListUserDto;

import java.util.List;

public interface UserService {
    List<ListUserDto> findAllUsers();
    DetailUserDto findUserById(Long id);
    User createNewUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
}
