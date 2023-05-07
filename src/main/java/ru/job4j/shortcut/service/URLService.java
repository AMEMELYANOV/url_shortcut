package ru.job4j.shortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.job4j.shortcut.model.URL;
import ru.job4j.shortcut.model.URLResponse;
import ru.job4j.shortcut.repository.SiteRepository;
import ru.job4j.shortcut.repository.URLRepository;

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
     * с внедренным кодом.
     *
     * @param url ссылка
     * @return объект URLResponse с внедренным кодом url
     */
    public URLResponse convertURL(URL url) {
        URL urlFromDB = urlRepository.findByurl(url.getUrl());
        String siteLogin = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long siteId = siteRepository.findByLogin(siteLogin).getId();
        if (urlFromDB != null && Objects.equals(siteId, urlFromDB.getSiteId())) {
            return new URLResponse(urlFromDB.getCode());
        }
        String urlCode = generatorService.generateURL();
        url.setSiteId(siteId);
        url.setCode(urlCode);
        urlRepository.save(url);
        return new URLResponse(urlCode);
    }

    /**
     * Вызывает метод репозитория для нахождения и возврат url по коду.
     *
     * @param code код url
     * @return объект url
     */
    public URL findByCode(String code) {
        return urlRepository.findByCode(code);
    }

    /**
     * Вызывает метод репозитория для выполнения увеличения счетчика посещений url
     * по переданному идентификатору url.
     *
     * @param id идентификатор url
     */
    public void incTotal(Long id) {
        urlRepository.incTotal(id);
    }
}
