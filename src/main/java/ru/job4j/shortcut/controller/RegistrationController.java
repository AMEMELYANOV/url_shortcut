package ru.job4j.shortcut.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @PostMapping("/")
    public ResponseEntity<String> create() {
        Person rsl = rest.postForObject(API, person, Person.class);
        return new ResponseEntity<>(
                rsl,
                HttpStatus.CREATED
        );
    }
}
