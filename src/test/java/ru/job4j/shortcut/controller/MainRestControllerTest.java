package ru.job4j.shortcut.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.job4j.shortcut.ShortcutApplication;
import ru.job4j.shortcut.model.*;
import ru.job4j.shortcut.service.SiteService;
import ru.job4j.shortcut.service.URLService;

import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Тест класс реализации контроллера
 *
 * @author Alexander Emelyanov
 * @version 1.0
 * @see ru.job4j.shortcut.controller.MainRestController
 */
@SpringBootTest(classes = ShortcutApplication.class)
@AutoConfigureMockMvc
class MainRestControllerTest {

    /**
     * Объект заглушка направления запросов
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Объект ObjectMapper
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Заглушка SiteService
     */
    @MockBean
    private SiteService siteService;

    /**
     * Заглушка URLService
     */
    @MockBean
    private URLService urlService;

    /**
     * Сайт-ответ
     */
    private SiteRegResponse siteRegResponse;

    /**
     * URL-ответ
     */
    private URLResponse urlResponse;

    /**
     * Сайт
     */
    private Site site;

    /**
     * URL ссылка
     */
    private URL url;

    /**
     * Создает необходимые для выполнения тестов общие объекты.
     * Создание выполняется перед каждым тестом.
     */
    @BeforeEach
    public void setup() {
        siteRegResponse = SiteRegResponse.builder()
                .registration("true")
                .login("responseLogin")
                .password("responsePass")
                .build();
        site = Site.builder()
                .id(1L)
                .site("site")
                .login("login")
                .password("pass")
                .build();
        url = URL.builder()
                .id(1L)
                .code("code")
                .url("url")
                .total(0)
                .siteId(1L)
                .build();
        urlResponse = URLResponse.builder()
                .code("code")
                .build();
    }

    /**
     * Направляется POST запрос на адрес /registration с телом запроса, содержащим данные регистрации
     * о сайте в формате JSON и выполняется проверка возвращения сообщение о регистрации сайта,
     * обернутое в объект siteRegResponse.
     */
    @Test
    @WithMockUser
    public void whenRegisterSiteThanReturnSiteRegResponse() throws Exception {
        doReturn(siteRegResponse).when(siteService).registerSite(site);

        this.mockMvc.perform(post("/registration")
                        .content(objectMapper.writeValueAsString(site))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registration").value("true"))
                .andExpect(jsonPath("$.login").value("responseLogin"))
                .andExpect(jsonPath("$.password").value("responsePass"));
    }

    /**
     * Направляется POST запрос на адрес /convert с телом запроса, содержащим данные url ссылки
     * в формате JSON и выполняется проверка возвращения сообщение с кодом url ссылки,
     * обернутое в объект urlResponse.
     */
    @Test
    @WithMockUser
    public void whenConvertUrlThanReturnUrlResponse() throws Exception {
        doReturn(urlResponse).when(urlService).convertURL(url);

        this.mockMvc.perform(post("/convert")
                        .content(objectMapper.writeValueAsString(url))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("code"));
    }

    /**
     * Направляется GET запрос на адрес /redirect/{code} и выполняется проверка
     * возвращения в хедерах ключа REDIRECT cо значением url ссылки.
     */
    @Test
    @WithMockUser
    public void whenRedirectThanReturnResponseEntityWithUrl() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("REDIRECT", url.getUrl());
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(
                httpHeaders, HttpStatus.MOVED_TEMPORARILY
        );
        doReturn(responseEntity).when(urlService).redirectUrl("code");

        this.mockMvc.perform(get("/redirect/{code}", "code"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers
                        .header().stringValues("REDIRECT", url.getUrl()));
    }

    /**
     * Направляется GET запрос на адрес /statistic выполняется проверка возвращения сообщение
     * с информацией о статистике сайта в виде списка объектов в формате JSON.
     */
    @Test
    @WithMockUser
    public void whenGetStatisticThanReturnListStatistic() throws Exception {
        Statistic statistic1 = Statistic.builder()
                .url("url1")
                .total(1)
                .build();
        Statistic statistic2 = Statistic.builder()
                .url("url2")
                .total(2)
                .build();
        doReturn(List.of(statistic1, statistic2)).when(siteService).getStatistic();

        this.mockMvc.perform(get("/statistic"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].url").value("url1"))
                .andExpect(jsonPath("$.[0].total").value(1))
                .andExpect(jsonPath("$.[1].url").value("url2"))
                .andExpect(jsonPath("$.[1].total").value(2));
    }
}