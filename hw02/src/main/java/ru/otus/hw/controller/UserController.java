package ru.otus.hw.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.otus.hw.dto.UserDto;
import ru.otus.hw.dto.UserSearchDto;
import ru.otus.hw.dto.View;
import ru.otus.hw.services.UserService;

@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/get/{id}")
    @JsonView(View.Public.class)
    public @ResponseBody ResponseEntity<?> getUserById(@PathVariable long id) {
        var user = userService.findById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("User with id '%d' is not found!".formatted(id), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/search")
    @JsonView(View.Public.class)
    public @ResponseBody ResponseEntity<?> getUserByNameAndSurname(@Valid @RequestBody UserSearchDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            var errors = handleValidationExceptions(bindingResult);
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        var users = userService.findByNameAndSurnamePrefix(userDto);
        if (users.isEmpty()) {
            return new ResponseEntity<>("Users are not found!", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            var errors = handleValidationExceptions(bindingResult);
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        if (userService.registerNewUserAccount(userDto)) {
            return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
    }

    private Map<String, String> handleValidationExceptions(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
