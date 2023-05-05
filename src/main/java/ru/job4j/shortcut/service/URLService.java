package ru.job4j.shortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.model.URL;
import ru.job4j.shortcut.model.URLResponse;
import ru.job4j.shortcut.repository.SiteRepository;
import ru.job4j.shortcut.repository.URLRepository;

@AllArgsConstructor
@Service
public class URLService {

    private final URLRepository urlRepository;
    private final SiteRepository siteRepository;
    private final GeneratorService generatorService;

    public URLResponse convertURL(URL url) {
        URL urlFromDB = urlRepository.findByurl(url.getUrl());
        String siteLogin = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long siteId = siteRepository.findByLogin(siteLogin).getId();
        if (urlFromDB != null && siteId == urlFromDB.getSiteId()) {
            return new URLResponse(urlFromDB.getCode());
        }
        String urlCode = generatorService.generateURL();
        url.setSiteId(siteId);
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
