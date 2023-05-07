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
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Сервис по работе с URL
 *
 * @author Alexander Emelyanov
 * @version 1.0
 */
@AllArgsConstructor
@Service
public class URLService {

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
     * с внедренным кодом. Если в базе не найдено url или сайт, будет
     * выброшено исключение.
     *
     * @param url ссылка
     * @return объект URLResponse с внедренным кодом url
     * @throws NoSuchElementException если url не найдено или не найден сайт
     */
    @Transactional
    public URLResponse convertURL(URL url) {
        URL urlFromDB = urlRepository.findByUrl(url.getUrl())
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("Url with name - '%s' not found", url.getUrl()))
                );
        String siteLogin = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Site site = siteRepository.findByLogin(siteLogin)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("Site with login - '%s' not found", siteLogin))
                );
        Long siteId = site.getId();
        if (Objects.equals(siteId, urlFromDB.getSiteId())) {
            return new URLResponse(urlFromDB.getCode());
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
