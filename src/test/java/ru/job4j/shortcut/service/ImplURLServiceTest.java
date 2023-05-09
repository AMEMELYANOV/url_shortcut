package ru.job4j.shortcut.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.job4j.shortcut.ShortcutApplication;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.model.URL;
import ru.job4j.shortcut.model.URLResponse;
import ru.job4j.shortcut.repository.SiteRepository;
import ru.job4j.shortcut.repository.URLRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

/**
 * Тест класс реализации сервисов
 *
 * @author Alexander Emelyanov
 * @version 1.0
 * @see ru.job4j.shortcut.service.ImplURLService
 */
@SpringBootTest(classes = ShortcutApplication.class)
class ImplURLServiceTest {

    /**
     * Объект для доступа к методам URLService
     */
    private URLService urlService;

    /**
     * Заглушка URLRepository
     */
    @MockBean
    private URLRepository urlRepository;

    /**
     * Заглушка SiteRepository
     */
    @MockBean
    private SiteRepository siteRepository;

    /**
     * Заглушка GeneratorService
     */
    @MockBean
    private GeneratorService generatorService;

    /**
     * Заглушка SecurityContext
     */
    @MockBean
    private SecurityContext securityContext;

    /**
     * Заглушка Authentication
     */
    @MockBean
    private Authentication authentication;

    /**
     * URL
     */
    private URL url;

    /**
     * Сайт
     */
    private Site site;

    /**
     * Создает необходимые для выполнения тестов общие объекты.
     * Создание выполняется перед каждым тестом.
     */
    @BeforeEach
    public void setup() {
        urlService = new ImplURLService(urlRepository, siteRepository,
                generatorService);

        url = URL.builder()
                .id(1L)
                .url("url")
                .code("code")
                .siteId(1L)
                .total(1)
                .build();

        site = Site.builder()
                .id(1L)
                .site("site")
                .login("login")
                .password("pass")
                .build();
    }

    /**
     * Выполняется конвертации url регистрации сайта, если url для текущего сайта
     * существует в базе.
     */
    @Test
    void whenConvertUrlThenReturnURLResponseIfPresent() {
        doReturn(List.of(url)).when(urlRepository).findByUrl(anyString());
        doReturn(authentication).when(securityContext).getAuthentication();
        doReturn(site.getLogin()).when(authentication).getPrincipal();
        SecurityContextHolder.setContext(securityContext);
        doReturn(Optional.of(site)).when(siteRepository).findByLogin(anyString());

        URLResponse urlResponse = urlService.convertURL(url);

        assertThat(urlResponse.getCode()).isEqualTo(url.getCode());
    }

    /**
     * Выполняется проверка конвертации url регистрации сайта, если url для
     * текущего сайта не существует в базе.
     */
    @Test
    void whenConvertUrlThenReturnURLResponseIfAbsent() {
        URL urlFromDB = URL.builder()
                .id(1L)
                .url("url")
                .code("code")
                .siteId(2L)
                .total(1)
                .build();
        doReturn(List.of(urlFromDB)).when(urlRepository).findByUrl(anyString());
        doReturn(authentication).when(securityContext).getAuthentication();
        doReturn(site.getLogin()).when(authentication).getPrincipal();
        SecurityContextHolder.setContext(securityContext);
        doReturn(Optional.of(site)).when(siteRepository).findByLogin(anyString());
        doReturn("genUrl").when(generatorService).generateURL();

        URLResponse urlResponse = urlService.convertURL(url);

        assertThat(urlResponse.getCode()).isEqualTo("genUrl");
    }

    /**
     * Выполняется проверка возврата адреса перенаправления url,
     * если url для кода существует в базе.
     */
    @Test
    void whenRedirectUrlThenReturnResponseEntity() {
        doReturn(Optional.of(url)).when(urlRepository).findByCode(anyString());

        ResponseEntity<Void> responseEntity = urlService.redirectUrl(anyString());

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(302);
        assertThat(responseEntity.getStatusCode().is3xxRedirection()).isEqualTo(true);
        assertThat(Objects.requireNonNull(responseEntity.getHeaders().get("REDIRECT")).get(0)).isEqualTo(url.getUrl());
    }

    /**
     * Выполняется проверка выброса исключения,
     * если url для кода не существует в базе.
     */
    @Test
    void whenRedirectUrlThenThrowsException() {
        doReturn(Optional.empty()).when(urlRepository).findByCode(anyString());
        assertThrows(NoSuchElementException.class,
                () -> urlService.redirectUrl(anyString()));
    }

}