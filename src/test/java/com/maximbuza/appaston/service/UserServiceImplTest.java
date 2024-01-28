package com.maximbuza.appaston.service;


import com.maximbuza.appaston.dto.ChangePasswordDTO;
import com.maximbuza.appaston.dto.UserDTO;
import com.maximbuza.appaston.exception.BadDataException;
import com.maximbuza.appaston.exception.ConflictException;
import com.maximbuza.appaston.exception.NotFoundException;
import com.maximbuza.appaston.exception.UnauthorizedException;
import com.maximbuza.appaston.storage.Storage;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserServiceImplTest extends UserServiceImpl {

    private UserDTO user;
    private ChangePasswordDTO changePasswordDTO;

    @Before
    public void init() {    // в этом блоке меняем hashmap на кастомный с данными, используя рефлексию.
        user = new UserDTO();
        changePasswordDTO = new ChangePasswordDTO();
        HashMap<String, String> userAccounts = new HashMap<>() {{
            put("Lil", "999");
            put("Max", "12345");
            put("Boot", "111112");
            put("Var", "54321");
        }};
        Field field;
        try {
            field = Storage.class.getDeclaredField("userAccounts");
            field.setAccessible(true);
            field.set(Storage.class, userAccounts);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @Test(expected = BadDataException.class) // 6 тестов сервиса смены пароля
    public void changePassword_WhenUsernameIncorrect() {
        changePasswordDTO.setUsername("");
        changePassword(changePasswordDTO);
    }

    @Test(expected = NotFoundException.class)
    public void changePassword_WhenUsernameNotFound() {
        changePasswordDTO.setUsername("Kira");
        changePassword(changePasswordDTO);
    }

    @Test(expected = BadDataException.class)
    public void changePassword_WhenOldPasswordIncorrect() {
        changePasswordDTO.setUsername("Lil");
        changePasswordDTO.setOldPassword("");
        changePasswordDTO.setNewPassword("2222");
        changePassword(changePasswordDTO);
    }

    @Test(expected = BadDataException.class)
    public void changePassword_WhenNewPasswordIncorrect() {
        changePasswordDTO.setUsername("Lil");
        changePasswordDTO.setOldPassword("3333");
        changePasswordDTO.setNewPassword("");
        changePassword(changePasswordDTO);
    }

    @Test(expected = UnauthorizedException.class)
    public void changePassword_WhenPasswordIsWrong() {
        changePasswordDTO.setUsername("Lil");
        changePasswordDTO.setOldPassword("99");
        changePasswordDTO.setNewPassword("2222");
        changePassword(changePasswordDTO);
    }

    @Test
    public void changePassword_WhenPasswordWasChanged() {
        changePasswordDTO.setUsername("Lil");
        changePasswordDTO.setOldPassword("999");
        changePasswordDTO.setNewPassword("2222");
        assertEquals(changePassword(changePasswordDTO), "Password was changed successfully. Your new login details:\nusername: " +
                changePasswordDTO.getUsername() + "\npassword: " + changePasswordDTO.getNewPassword());
    }


    @Test(expected = BadDataException.class) // 5 тестов сервиса по входу юзера
    public void signInUser_WhenUsernameIncorrect() {
        user.setUsername("");
        user.setPassword("4444");
        signInUser(user);
    }

    @Test(expected = NotFoundException.class)
    public void signInUser_WhenUsernameNotFound() {
        user.setUsername("Kira");
        user.setPassword("4444");
        signInUser(user);
    }

    @Test(expected = BadDataException.class)
    public void signInUser_WhenPasswordIncorrect() {
        user.setUsername("Lil");
        user.setPassword("");
        signInUser(user);
    }

    @Test(expected = UnauthorizedException.class)
    public void signInUser_WhenPasswordIsWrong() {
        user.setUsername("Lil");
        user.setPassword("1999");
        signInUser(user);
    }

    @Test
    public void signInUser_WhenAllCorrect() {
        user.setUsername("Lil");
        user.setPassword("999");
        assertEquals(signInUser(user), "Successful login. Congratulations");
    }


    @Test(expected = BadDataException.class) // 5 тестов сервиса регистрации юзера
    public void signUpUser_WhenUsernameIncorrect() {
        user.setUsername("");
        user.setPassword("123");
        assertEquals(signUpUser(user), "Username is incorrect format1");
    }

    @Test(expected = ConflictException.class)
    public void signUpUser_WhenUserHasAlreadyAdded() {
        user.setUsername("Lil");
        user.setPassword("123");
        signUpUser(user);
    }

    @Test(expected = BadDataException.class)
    public void signUpUser_WhenPasswordIncorrect() {
        user.setUsername("Mks");
        user.setPassword("");
        signUpUser(user);
    }

    @Test
    public void signUpUser_WhenUserHasBeenAdded() {
        user.setUsername("Mks");
        user.setPassword("321");
        assertEquals(signUpUser(user), "User has been added:\nlogin: " + user.getUsername() + "\npassword: " + user.getPassword());

    }

    @Test // 2 теста метода по проверке корректности юзернейма
    public void isUsernameIncorrect_ShouldBeTrue_WhenUserEmpty() {
        assertTrue(isUserIncorrect(""));
    }

    @Test
    public void isUsernameIncorrect_ShouldBeFalse_WhenUserNotEmpty() {
        assertFalse(isUserIncorrect("Mks"));
    }


    @Test // 2 теста метода по проверке корректности пароля
    public void isPasswordIncorrectFormat_ShouldBeTrue_WhenPasswordEmpty() {
        assertTrue(isPasswordIncorrectFormat(""));
    }

    @Test
    public void isPasswordIncorrectFormat_ShouldBeFalse_WhenPasswordNotEmpty() {
        assertFalse(isPasswordIncorrectFormat("12345"));
    }

    @Test // 2 теста метода по проверки совпадения пароля с хранилищем
    public void isPasswordMatch_ShouldFalse_WhenPasswordNotMatch() {
        assertFalse(isPasswordMatch("Max", "00000"));
    }

    @Test
    public void isPasswordMatch_ShouldTrue_WhenPasswordMatch() {
        assertTrue(isPasswordMatch("Boot", "111112"));
    }
}
