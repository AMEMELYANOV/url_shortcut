package ru.job4j.shortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.model.URL;
import ru.job4j.shortcut.model.URLResponse;
import ru.job4j.shortcut.repository.SiteRepository;
import ru.job4j.shortcut.repository.URLRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

/**
 * Реализация сервиса по работе с URL
 *
 * @see ru.job4j.shortcut.service.URLService
 * @author Alexander Emelyanov
 * @version 1.0
 */
@AllArgsConstructor
@Service
public class ImplURLService implements URLService {

    /**
     * Объект для доступа к методам URLRepository
     */
    private final URLRepository urlRepository;

    /**
     * Объект для доступа к методам SiteRepository
     */
    private final SiteRepository siteRepository;

    /**
     * Объект для доступа к методам GeneratorService
     */
    private final GeneratorService generatorService;

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
    @Transactional
    @Override
    public URLResponse convertURL(URL url) {
        List<URL> urlsFromDB = urlRepository.findByUrl(url.getUrl());
        String siteLogin = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Site site = siteRepository.findByLogin(siteLogin)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("Site with login - '%s' not found", siteLogin))
                );
        Optional<URL> urlFromDB = urlsFromDB.stream().filter(u -> u.getSiteId().equals(site.getId())).findFirst();
        Long siteId = site.getId();
        if (urlFromDB.isPresent() && Objects.equals(siteId, urlFromDB.get().getSiteId())) {
            return new URLResponse(urlFromDB.get().getCode());
        }
        String urlCode = generatorService.generateURL();
        url.setSiteId(siteId);
        url.setCode(urlCode);
        urlRepository.save(url);
        return new URLResponse(urlCode);
    }

    /**
     * Вызывает метод репозитория для нахождения url по коду url
     * и возврат ResponseEntity c url. Если код введен неверно
     * будет выброшено исключение с сообщением о неправильно введенных данных.
     *
     * @param code код url
     * @return объект ResponseEntity c url
     * @throws NoSuchElementException если url c переданным кодом не найден
     */
    @Transactional
    @Override
    public ResponseEntity<Void> redirectUrl(String code) {
        URL url = urlRepository.findByCode(code)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("URL with code - '%s' not found", code))
                );
        urlRepository.incTotal(url.getId());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("REDIRECT", url.getUrl());
        return new ResponseEntity<>(
                httpHeaders, HttpStatus.MOVED_TEMPORARILY
        );
    }
}
