package ru.job4j.shortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.model.SiteRegResponse;
import ru.job4j.shortcut.model.Statistic;
import ru.job4j.shortcut.model.URL;
import ru.job4j.shortcut.repository.SiteRepository;
import ru.job4j.shortcut.repository.URLRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Сервис по работе с сайтами
 *
 * @author Alexander Emelyanov
 * @version 1.0
 */
@AllArgsConstructor
@Service
public class SiteService {

    /**
     * Объект для доступа к методам SiteRepository
     */
    private final SiteRepository siteRepository;

    /**
     * Объект для доступа к методам GeneratorService
     */
    private final GeneratorService generatorService;

    /**
     * Объект для доступа к методам URLRepository
     */
    private final URLRepository urlRepository;

    /**
     * Шифратор паролей
     */
    private final BCryptPasswordEncoder encoder;

    /**
     * Выполняет создание и возврат нового объекта SiteRegResponse,
     * который содержит сгенерированные поля логина и пароля для сайта.
     * Если сайт уже зарегистрирован, вернется ответ с параметром registration
     * равным false.
     *
     * @param site сайт
     * @return новый объект SiteRegResponse
     */
    public SiteRegResponse registerSite(Site site) {
        Site siteFromDB = siteRepository.findBySite(site.getSite());
        if (siteFromDB != null) {
            return new SiteRegResponse("false", siteFromDB.getLogin(), "Сайт зарегистрирован ранее!");
        }
        String password = generatorService.generatePassword();
        site.setLogin(generatorService.generateLogin());
        site.setPassword(encoder.encode(password));
        Site newSite = siteRepository.save(site);
        return new SiteRegResponse("true", newSite.getLogin(), password);
    }

    /**
     * Выполняет вызов метода репозитория поиска сайта по логину.
     *
     * @param login логин сайта
     * @return сайт
     */
    public Site findByLogin(String login) {
        return siteRepository.findByLogin(login);
    }

    /**
     * Выполняет возврат списка объектов статистики по текущему сайту,
     * вызывая метод репозитория.
     * Если сайт не найден, будет возвращен пустой список.
     *
     * @return список объектов статистики
     */
    public List<Statistic> getStatistic() {
        String siteLogin = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Site site = siteRepository.findByLogin(siteLogin);
        if (site == null) {
            return Collections.emptyList();
        }
        Long siteId = site.getId();
        List<URL> urls = urlRepository.findAllBySiteId(siteId);
        List<Statistic> result = new ArrayList<>();
        for (URL url : urls) {
            result.add(new Statistic(url.getUrl(), url.getTotal()));
        }
        return result;
    }
}
