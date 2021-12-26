package ru.job4j.shortcut.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;

@Service
public class GeneratorService {

    @Value("${generator.login.length}")
    private int loginLength;
    @Value("${generator.password.length}")
    private int passwordLength;
    @Value("${generator.URL.length}")
    private int urlLength;

    public String generateLogin() {
        return RandomStringUtils.randomAlphanumeric(loginLength);
    }

    public String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(passwordLength);
    }

    public String generateURL() {
        return RandomStringUtils.randomAlphanumeric(urlLength);
    }
}
