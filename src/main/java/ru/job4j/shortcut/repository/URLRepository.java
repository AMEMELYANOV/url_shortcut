package ru.job4j.shortcut.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.shortcut.model.URL;

import java.util.List;

/**
 * Хранилище url ссылок
 *
 * @see ru.job4j.shortcut.model.URL
 * @author Alexander Emelyanov
 * @version 1.0
 */
public interface URLRepository extends CrudRepository<URL, Long> {

    /**
     * Выполняет поиск и возврат url ссылки по наименованию.
     *
     * @param url наименование url ссылки
     * @return url ссылка
     */
    URL findByurl(String url);

    /**
     * Выполняет поиск и возврат url ссылки по коду.
     *
     * @param code код url ссылки
     * @return url ссылка
     */
    URL findByCode(String code);

    /**
     * Выполняет увеличение счетчика посещений url.
     *
     * @param id идентификатор url
     */
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update URL u set u.total = u.total + 1 where u.id = :id")
    void incTotal(Long id);

    /**
     * Выполняет поиск и возврат списка url ссылок
     * по идентификатору сайта.
     *
     * @param id  идентификатор сайта
     * @return список url
     */
    List<URL> findAllBySiteId(Long id);
}
