package com.education.eduprime.service;

import com.education.eduprime.model.User;
import com.education.eduprime.model.dto.DetailUserDto;
import com.education.eduprime.model.dto.ListUserDto;
import com.education.eduprime.repository.UserRepository;
import com.education.eduprime.utils.ModelMapperUtil;
import com.education.eduprime.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapperUtil mapperUtil;

    @Override
    public List<ListUserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        List<ListUserDto> usersDtos = mapperUserToDto(users);

        return usersDtos;
    }

    @Override
    public DetailUserDto findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id :" + id));
        DetailUserDto userDto = mapperUserToDto(user);

        return userDto;
    }

    @Override
    public User createNewUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User payload) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id :" + id));

        user.setUserName(payload.getUserName());
        user.setAddress(payload.getAddress());
        user.setAge(payload.getAge());

        userRepository.save(user);

        return user;
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id :" + id));

        userRepository.delete(user);
    }

    private List<ListUserDto> mapperUserToDto(List<User> users) {
        List<ListUserDto> listUserDtos = new ArrayList<>();

        for(User user: users) {
            ListUserDto listUserDto = mapperUtil
                    .modelMapperUtility()
                    .map(user, ListUserDto.class);

            listUserDto.setUserName(user.getUserName());
            listUserDto.setAge(user.getAge());
            listUserDto.setAddress(user.getAddress());
            listUserDtos.add(listUserDto);
        }

        return listUserDtos;
    }

    private DetailUserDto mapperUserToDto(User user) {
        DetailUserDto detailUserDto = mapperUtil
                .modelMapperUtility()
                .map(user, DetailUserDto.class);

        detailUserDto.setUserName(user.getUserName());
        detailUserDto.setAge(user.getAge());
        detailUserDto.setAddress(user.getAddress());

        return detailUserDto;
    }
}
