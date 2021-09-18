package com.education.eduprime.presenter;

import com.education.eduprime.model.User;
import com.education.eduprime.model.dto.DetailUserDto;
import com.education.eduprime.model.dto.ListUserDto;
import com.education.eduprime.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1")
public class UserPresenter {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping(value = "/users", produces = "application/json")
    public Map<String, Object> allUsers() {
        Map<String, Object> map = new HashMap<>();
        List<ListUserDto> listUserDtos = userService.findAllUsers();

        map.put("data", listUserDtos);
        return map;
    }

    @GetMapping("/users/{id}")
    public Map<String, Object> detailUser(@PathVariable Long id) {
        Map<String, Object> map = new HashMap<>();
        DetailUserDto detailUser = userService.findUserById(id);

        map.put("data", detailUser);
        return map;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> createUser(@RequestBody User payload) {
        Map<String, Object> map = new HashMap<>();

        User user = userService.createNewUser(payload);

        map.put("data", user);
        return map;
    }

    @PutMapping("/users/{id}")
    public Map<String, Object> updateUser(
            @PathVariable Long id,
            @RequestBody User payload) {
        Map<String, Object> map = new HashMap<>();

        User user = userService.updateUser(id, payload);

        map.put("data", user);
        return map;
    }

    @DeleteMapping("/users/{id}")
    public Map<String, Object> deleteUser(@PathVariable Long id) {
        Map<String, Object> map = new HashMap<>();

        userService.deleteUser(id);

        map.put("data", "");
        return map;
    }
}
