package ru.job4j.shortcut.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.shortcut.model.Site;

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
    Site findBySite(String site);

    /**
     * Выполняет поиск и возврат сайта по логину.
     *
     * @param login логин
     * @return сайт
     */
    Site findByLogin(String login);
}
