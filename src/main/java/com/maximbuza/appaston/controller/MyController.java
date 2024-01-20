package com.maximbuza.appaston.controller;


import com.maximbuza.appaston.dto.LoginAndRegistrationUserRequestDTO;
import com.maximbuza.appaston.dto.ChangerPasswordRequestDTO;
import com.maximbuza.appaston.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api") // к этому контроллеру обращаться через /api
public class MyController {
    public MyController(@Autowired UserService userService) {
        this.userService = userService;
    } // прикрепили сервис к контроллеру

    private final UserService userService;


    @GetMapping("/getAllUsers") // по этому адресу можно вызвать get запрос и получить список юзеров
    public String getAllUsers() {
        return userService.getAllUsers();
    } // делегирует запрос сервису

    @PostMapping("/signUpUser") //регистрация юзера
    public String signUpUser(@RequestBody LoginAndRegistrationUserRequestDTO loginAndRegistrationUserRequestDTO) { // образует контейнер данных пользователя и передает сервису
        return userService.singUpUser(loginAndRegistrationUserRequestDTO);
    }

    @PostMapping("/signInUser") //вход юзера
    public String signInUser(@RequestBody LoginAndRegistrationUserRequestDTO loginAndRegistrationUserRequestDTO) { // образует контейнер данных пользователя и передает сервису
        return userService.singInUser(loginAndRegistrationUserRequestDTO);
    }

    @PostMapping("/changePassword") //смена пароля
    public String changePassword(@RequestBody ChangerPasswordRequestDTO changerPasswordRequestDTO) { // образует контейнер данных пользователя и передает сервису
        return userService.changePassword(changerPasswordRequestDTO);
    }
}


