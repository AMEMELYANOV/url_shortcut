package ru.job4j.shortcut.service;

import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.model.SiteRegResponse;
import ru.job4j.shortcut.model.Statistic;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Сервис по работе с сайтами
 *
 * @author Alexander Emelyanov
 * @version 1.0
 */
public interface SiteService {

    /**
     * Выполняет создание и возврат нового объекта SiteRegResponse,
     * который содержит сгенерированные поля логина и пароля для сайта.
     * Если сайт уже зарегистрирован, вернется ответ с параметром registration
     * равным false.
     *
     * @param site сайт
     * @return новый объект SiteRegResponse
     */
    SiteRegResponse registerSite(Site site);

    /**
     * Выполняет вызов метода репозитория поиска сайта по логину.
     * Если сайт не найден, будет выброшено исключение.
     *
     * @param login логин сайта
     * @return сайт
     * @throws NoSuchElementException если сайт по логину не найден
     */
    Site findByLogin(String login);

    /**
     * Выполняет возврат списка объектов статистики по текущему сайту,
     * вызывая метод репозитория. Если сайт не найден, будет выброшено исключение.
     *
     * @return список объектов статистики
     * @throws NoSuchElementException если сайт не найден
     */
    List<Statistic> getStatistic();
}
