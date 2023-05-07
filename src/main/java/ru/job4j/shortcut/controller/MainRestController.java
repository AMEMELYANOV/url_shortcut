package ru.job4j.shortcut.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.shortcut.model.*;
import ru.job4j.shortcut.service.SiteService;
import ru.job4j.shortcut.service.URLService;

import java.util.List;

/**
 * Контроллер основных действий с приложением
 *
 * @author Alexander Emelyanov
 * @version 1.0
 */
@AllArgsConstructor
@RestController
public class MainRestController {

    /**
     * Объект для доступа к методам SiteService
     */
    private final SiteService siteService;

    /**
     * Объект для доступа к методам URLService
     */
    private final URLService urlService;

    /**
     * Обрабатывает POST запрос, выполняет передачу сайта на
     * сервисный слой и возврат ответа о результатах регистрации.
     *
     * @param site сайт
     * @return результат регистрации сайта
     */
    @PostMapping("/registration")
    public SiteRegResponse registerSite(@RequestBody Site site) {
        return siteService.registerSite(site);
    }

    /**
     * Обрабатывает POST запрос, выполняет передачу сайта на
     * сервисный слой и возврат ответа с закодированным URL.
     *
     * @param url ссылка
     * @return закодированный URL
     */
    @PostMapping("/convert")
    public URLResponse convertURL(@RequestBody URL url) {
        return urlService.convertURL(url);
    }

    /**
     * Обрабатывает GET запрос, выполняет передачу сайта на
     * сервисный слой и возврат ответа реального URL ресурса.
     * Ответ добавляется в заголовки ответа приложения.
     *
     * @param code код url
     * @return объект ResponseEntity
     */
    @GetMapping("/redirect/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        URL url = urlService.findByCode(code);
        if (url == null) {
            return new ResponseEntity<>(
                    HttpStatus.NOT_FOUND
            );
        }
        urlService.incTotal(url.getId());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("REDIRECT", url.getUrl());
        return new ResponseEntity<>(
                httpHeaders, HttpStatus.MOVED_TEMPORARILY
        );
    }

    /**
     * Обрабатывает GET запрос, выполняет возврат статистики посещений
     * по затребованному сайту.
     *
     * @return список с объектами статистики
     */
    @GetMapping("/statistic")
    public List<Statistic> getStatistic() {
        return siteService.getStatistic();
    }
}
