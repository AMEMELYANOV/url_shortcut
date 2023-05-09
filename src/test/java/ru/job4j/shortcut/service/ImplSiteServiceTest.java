package ru.job4j.shortcut.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.job4j.shortcut.ShortcutApplication;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.model.SiteRegResponse;
import ru.job4j.shortcut.model.Statistic;
import ru.job4j.shortcut.model.URL;
import ru.job4j.shortcut.repository.SiteRepository;
import ru.job4j.shortcut.repository.URLRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

/**
 * Тест класс реализации сервисов
 *
 * @author Alexander Emelyanov
 * @version 1.0
 * @see ru.job4j.shortcut.service.ImplSiteService
 */
@SpringBootTest(classes = ShortcutApplication.class)
class ImplSiteServiceTest {

    /**
     * Объект для доступа к методам SiteService
     */
    private SiteService siteService;

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
     * Заглушка GeneratorService
     */
    @MockBean
    private GeneratorService generatorService;

    /**
     * Заглушка SiteRepository
     */
    @MockBean
    private SiteRepository siteRepository;

    /**
     * Заглушка URLRepository
     */
    @MockBean
    private URLRepository urlRepository;

    /**
     * Заглушка шифратора паролей BCryptPasswordEncoder
     */
    @MockBean
    private BCryptPasswordEncoder encoder;

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
        siteService = new ImplSiteService(siteRepository, generatorService,
                urlRepository, encoder);

        site = Site.builder()
                .id(1L)
                .site("site")
                .login("login")
                .password("pass")
                .build();
    }

    /**
     * Выполняется проверка регистрации сайта, если сайт не был ранее зарегистрирован.
     */
    @Test
    void whenRegisterSiteThenReturnTrueAnswer() {
        doReturn(Optional.empty()).when(siteRepository).findBySite(anyString());
        doReturn("genPass").when(generatorService).generatePassword();
        doReturn("genLogin").when(generatorService).generateLogin();
        doReturn(site).when(siteRepository).save(site);

        SiteRegResponse siteRegResponse = siteService.registerSite(site);

        assertThat(siteRegResponse.getRegistration()).isEqualTo("true");
        assertThat(siteRegResponse.getLogin()).isEqualTo("genLogin");
        assertThat(siteRegResponse.getPassword()).isEqualTo("genPass");
    }

    /**
     * Выполняется проверка регистрации сайта, если сайт был ранее зарегистрирован.
     */
    @Test
    void whenRegisterSiteThenReturnFalseAnswer() {
        doReturn(Optional.of(site)).when(siteRepository).findBySite(anyString());

        SiteRegResponse siteRegResponse = siteService.registerSite(site);

        assertThat(siteRegResponse.getRegistration()).isEqualTo("false");
        assertThat(siteRegResponse.getLogin()).isEqualTo("login");
        assertThat(siteRegResponse.getPassword()).isEqualTo("Сайт зарегистрирован ранее!");
    }

    /**
     * Выполняется проверка результата вызова метода поиска сайта по логину,
     * если сайт найден.
     */
    @Test
    void whenFindByLoginThenReturnSite() {
        doReturn(Optional.of(site)).when(siteRepository).findByLogin(anyString());

        Site siteFromDB = siteService.findByLogin(anyString());

        assertThat(siteFromDB.getId()).isEqualTo(site.getId());
        assertThat(siteFromDB.getLogin()).isEqualTo(site.getLogin());
        assertThat(siteFromDB.getPassword()).isEqualTo(site.getPassword());
        assertThat(siteFromDB.getSite()).isEqualTo(site.getSite());
    }

    /**
     * Выполняется проверка выброса исключения методом поиска сайта по логину,
     * если сайт не найден.
     */
    @Test
    void whenFindByLoginThenThrowsException() {
        doReturn(Optional.empty()).when(siteRepository).findByLogin(anyString());

        assertThrows(NoSuchElementException.class,
                () -> siteService.findByLogin(anyString()));
    }

    /**
     * Выполняется проверка метода получения статистики по всем url.
     */
    @Test
    void whenGetStatisticThenReturnList() {
        doReturn(authentication).when(securityContext).getAuthentication();
        doReturn(site.getLogin()).when(authentication).getPrincipal();
        SecurityContextHolder.setContext(securityContext);
        doReturn(Optional.of(site)).when(siteRepository).findByLogin(anyString());
        URL url1 = URL.builder()
                .id(1L)
                .url("url1")
                .code("code1")
                .siteId(1L)
                .total(1)
                .build();
        URL url2 = URL.builder()
                .id(2L)
                .url("url2")
                .code("code2")
                .siteId(1L)
                .total(2)
                .build();
        List<URL> urls = List.of(url1, url2);
        Statistic statistic1 = Statistic.builder()
                .url(url1.getUrl())
                .total(url1.getTotal())
                .build();
        Statistic statistic2 = Statistic.builder()
                .url(url2.getUrl())
                .total(url2.getTotal())
                .build();
        List<Statistic> statistics = List.of(statistic1, statistic2);
        doReturn(urls).when(urlRepository).findAllBySiteId(anyLong());

        List<Statistic> statisticFromDB = siteService.getStatistic();

        assertThat(statisticFromDB.size()).isEqualTo(2);
        assertThat(statisticFromDB.get(0).getUrl()).isEqualTo(url1.getUrl());
        assertThat(statisticFromDB.get(0).getTotal()).isEqualTo(url1.getTotal());
        assertThat(statisticFromDB.get(1).getUrl()).isEqualTo(url2.getUrl());
        assertThat(statisticFromDB.get(1).getTotal()).isEqualTo(url2.getTotal());
    }

}