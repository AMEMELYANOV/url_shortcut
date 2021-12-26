package ru.job4j.shortcut.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.shortcut.model.*;
import ru.job4j.shortcut.service.SiteService;
import ru.job4j.shortcut.service.URLService;

import java.util.List;

@RestController
public class MainRestController {
    private final SiteService siteService;
    private final URLService urlService;

    public MainRestController(SiteService siteService, URLService urlService) {
        this.siteService = siteService;
        this.urlService = urlService;
    }

    @PostMapping("/registration")
    public SiteRegResponse registerSite(@RequestBody Site site) {
        return siteService.registerSite(site);
    }

    @PostMapping("/convert")
    public URLResponse convertURL(@RequestBody URL url) {
        return urlService.convertURL(url);
    }

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

    @GetMapping("/statistic")
    public List<Statistic> getStatistic() {
        return siteService.getStatistic();
    }
}
