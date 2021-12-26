package ru.job4j.shortcut.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.model.URL;
import ru.job4j.shortcut.model.URLResponse;
import ru.job4j.shortcut.repository.SiteRepository;
import ru.job4j.shortcut.repository.URLRepository;

@Service
public class URLService {
    private final URLRepository urlRepository;
    private final SiteRepository siteRepository;
    private final GeneratorService generatorService;

    public URLService(URLRepository urlRepository, SiteRepository siteRepository,
                      GeneratorService generatorService) {
        this.urlRepository = urlRepository;
        this.siteRepository = siteRepository;
        this.generatorService = generatorService;
    }


    public URLResponse convertURL(URL url) {
        URL urlFromDB = urlRepository.findByurl(url.getUrl());
        if (urlFromDB != null) {
            return new URLResponse(urlFromDB.getCode());
        }
        String siteLogin = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Site site = siteRepository.findByLogin(siteLogin);
        String urlCode = generatorService.generateURL();
        url.setSiteId(site.getId());
        url.setCode(urlCode);
        urlRepository.save(url);
        return new URLResponse(urlCode);
    }

    public URL findByCode(String code) {
        return urlRepository.findByCode(code);
    }

    public void incTotal(Long id) {
        urlRepository.incTotal(id);
    }
}
