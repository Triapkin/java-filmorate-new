package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class UserTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void test_userWithAllValidField() {
        User user = User.builder()
                .id(1)
                .email("valid@example.com")
                .login("validLogin")
                .name("valid name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Пользователь должен быть валидным");
    }

    @Test
    public void test_userWithEmptyEmail() {
        User user = User.builder()
                .id(1)
                .email("")
                .login("validLogin")
                .name("valid name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Email не может быть пустым");
    }

    @Test
    public void test_userWithInvalidEmail() {
        User user = User.builder()
                .id(1)
                .email("invalidEMAIL")
                .login("validLogin")
                .name("valid name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Email должен содержать символ @");
    }

    @Test
    public void test_userWithEmptyLogin() {
        User user = User.builder()
                .id(1)
                .email("")
                .login("")
                .name("valid name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Логин не может быть пустым");
    }

    @Test
    public void test_userLoginWithSpaces() {
        User user = User.builder()
                .id(1)
                .email("")
                .login("invalid login")
                .name("valid name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Логин не должен сожержать пустых значений");
    }

    @Test
    public void test_userWithNullBirthday() {
        User user = User.builder()
                .id(1)
                .email("")
                .login("validLogin")
                .name("valid name")
                .birthday(null)
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "День рождение пользователя не может быть null");
    }

    @Test
    public void test_userHaveFutureBirthday() {
        User user = User.builder()
                .id(1)
                .email("")
                .login("validLogin")
                .name("valid name")
                .birthday(LocalDate.now().plusDays(1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "День рождение пользователя не может быть в будущем");
    }

    @Test
    public void test_userWithEmptyNameUsesLogin() {
        User user = new User(1, "valid@example.com", "validLogin", "", LocalDate.of(2000, 1, 1));
        user.setName("");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Имя может быть пустым");
        assertEquals("validLogin", user.getName(), "Если Имя пользователя пустое, то должен быть логин");
    }
}
