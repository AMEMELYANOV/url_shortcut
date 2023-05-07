package ru.job4j.shortcut.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Сервис генератор
 *
 * @author Alexander Emelyanov
 * @version 1.0
 */
@Service
public class GeneratorService {

    /**
     * Длина логина, значение задается в application.yaml
     */
    @Value("${generator.login.length}")
    private int loginLength;

    /**
     * Длина пароля, значение задается в application.yaml
     */
    @Value("${generator.password.length}")
    private int passwordLength;

    /**
     * Длина url кода, значение задается в application.yaml
     */
    @Value("${generator.URL.length}")
    private int urlLength;

    /**
     * Выполняет генерацию и возврат строки логина.
     *
     * @return строка логина
     */
    public String generateLogin() {
        return RandomStringUtils.randomAlphanumeric(loginLength);
    }

    /**
     * Выполняет генерацию и возврат строки пароля.
     *
     * @return строка пароля
     */
    public String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(passwordLength);
    }

    /**
     * Выполняет генерацию и возврат строки кода url.
     *
     * @return строка кода url
     */
    public String generateURL() {
        return RandomStringUtils.randomAlphanumeric(urlLength);
    }
}
