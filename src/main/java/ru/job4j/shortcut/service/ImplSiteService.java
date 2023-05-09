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

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Реализация сервиса по работе с сайтами
 *
 * @see ru.job4j.shortcut.service.SiteService
 * @author Alexander Emelyanov
 * @version 1.0
 */
@AllArgsConstructor
@Service
public class ImplSiteService implements SiteService {

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
    @Transactional
    @Override
    public SiteRegResponse registerSite(Site site) {
        Optional<Site> siteFromDB = siteRepository.findBySite(site.getSite());
        if (siteFromDB.isPresent()) {
            return new SiteRegResponse("false", siteFromDB.get().getLogin(),
                    "Сайт зарегистрирован ранее!");
        }
        String password = generatorService.generatePassword();
        site.setLogin(generatorService.generateLogin());
        site.setPassword(encoder.encode(password));
        Site newSite = siteRepository.save(site);
        return new SiteRegResponse("true", newSite.getLogin(), password);
    }

    /**
     * Выполняет вызов метода репозитория поиска сайта по логину.
     * Если сайт не найден, будет выброшено исключение.
     *
     * @param login логин сайта
     * @return сайт
     * @throws NoSuchElementException если сайт по логину не найден
     */
    @Transactional
    @Override
    public Site findByLogin(String login) {
        return siteRepository.findByLogin(login)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("Site with login - '%s' not found", login))
                );
    }

    /**
     * Выполняет возврат списка объектов статистики по текущему сайту,
     * вызывая метод репозитория. Если сайт не найден, будет выброшено исключение.
     *
     * @return список объектов статистики
     * @throws NoSuchElementException если сайт не найден
     */
    @Transactional
    @Override
    public List<Statistic> getStatistic() {
        String siteLogin = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Site site = siteRepository.findByLogin(siteLogin)
                .orElseThrow(() -> new NoSuchElementException(
                String.format("Site with login - '%s' not found", siteLogin))
        );
        Long siteId = site.getId();
        List<URL> urls = urlRepository.findAllBySiteId(siteId);
        List<Statistic> result = new ArrayList<>();
        urls.forEach(url -> result.add(new Statistic(url.getUrl(), url.getTotal())));
        return result;
    }
}
