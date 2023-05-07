package ru.job4j.shortcut.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.shortcut.model.*;
import ru.job4j.shortcut.service.SiteService;
import ru.job4j.shortcut.service.URLService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Контроллер основных действий с приложением
 *
 * @author Alexander Emelyanov
 * @version 1.0
 */
@Slf4j
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
     * Объект для отображения данных
     */
    private final ObjectMapper objectMapper;

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
     * Обрабатывает POST запрос, выполняет передачу кода url на
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
     * Обрабатывает GET запрос, выполняет передачу кода url на
     * сервисный слой и возврат ответа реального URL ресурса.
     *
     * @param code код url
     * @return объект ResponseEntity
     */
    @GetMapping("/redirect/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        return urlService.redirectUrl(code);
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

    /**
     * Выполняет локальный (уровня контроллера) перехват исключений
     * NoSuchElementException, в случае перехвата, выполняет логирование
     * и возвращает клиенту ответ с комментарием исключения.
     *
     * @param e перехваченное исключение
     * @param response ответ клиенту
     */
    @ExceptionHandler(value = { NoSuchElementException.class })
    public void exceptionHandler(Exception e, HttpServletResponse response)
            throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
            put("message", e.getMessage());
            put("type", e.getClass());
        }}));
        log.error(e.getLocalizedMessage());
    }
}
