package com.home.eschool.controller;

import com.home.eschool.entity.Users;
import com.home.eschool.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public List<Users> usersList() {
        return userService.getUsersList();
    }

    @PostMapping("/create")
    public Users create(@RequestBody Users userData) {
        return userService.createUser(userData);
    }

    @PostMapping("/delete")
    public void delete(@RequestBody List<UUID> ids) {
        userService.deleteUsers(ids);
    }
}
