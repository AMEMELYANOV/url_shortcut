package ru.job4j.shortcut.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.shortcut.model.Site;

@Repository
public interface SiteRepository extends CrudRepository<Site, Long> {

    Site findBySite(String site);

    Site findByLogin(String login);
}
