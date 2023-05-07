package ru.job4j.shortcut.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.shortcut.model.Site;

import java.util.Optional;

/**
 * Хранилище сайтов
 *
 * @see ru.job4j.shortcut.model.Site
 * @author Alexander Emelyanov
 * @version 1.0
 */
@Repository
public interface SiteRepository extends CrudRepository<Site, Long> {

    /**
     * Выполняет поиск и возврат сайта по наименованию.
     *
     * @param site наименование сайта
     * @return сайт
     */
    Optional<Site> findBySite(String site);

    /**
     * Выполняет поиск и возврат сайта по логину.
     *
     * @param login логин
     * @return сайт
     */
    Optional<Site> findByLogin(String login);
}
