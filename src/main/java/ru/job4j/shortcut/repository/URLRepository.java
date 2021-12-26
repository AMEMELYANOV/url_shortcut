package ru.job4j.shortcut.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.shortcut.model.URL;

import java.util.List;

public interface URLRepository extends CrudRepository<URL, Long> {
    URL findByurl(String url);

    URL findByCode(String code);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update URL u set u.total = u.total + 1 where u.id = :id")
    void incTotal(Long id);

    List<URL> findAllBySiteId(Long id);
}
