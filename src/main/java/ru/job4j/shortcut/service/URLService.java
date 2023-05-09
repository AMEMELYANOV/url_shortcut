package ru.job4j.shortcut.service;

import org.springframework.http.ResponseEntity;
import ru.job4j.shortcut.model.URL;
import ru.job4j.shortcut.model.URLResponse;

import java.util.NoSuchElementException;

/**
 * Сервис по работе с URL
 *
 * @author Alexander Emelyanov
 * @version 1.0
 */
public interface URLService {

    /**
     * Выполняет конвертацию url в символьный код. Если url
     * отсутствует в базе, происходит сохранение нового объекта
     * через метод репозитория. Возвращает новый объект URLResponse
     * с внедренным кодом. Если в базе не найден сайт, будет
     * выброшено исключение.
     *
     * @param url ссылка
     * @return объект URLResponse с внедренным кодом url
     * @throws NoSuchElementException если не найден сайт
     */
    URLResponse convertURL(URL url);

    /**
     * Вызывает метод репозитория для нахождения url по коду url
     * и возврат ResponseEntity c url. Если код введен неверно
     * будет выброшено исключение с сообщением о неправильно введенных данных.
     *
     * @param code код url
     * @return объект ResponseEntity c url
     * @throws NoSuchElementException если url c переданным кодом не найден
     */
    ResponseEntity<Void> redirectUrl(String code);
}
