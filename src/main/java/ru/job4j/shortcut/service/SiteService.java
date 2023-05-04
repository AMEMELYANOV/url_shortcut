package ru.job4j.shortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.model.SiteRegResponse;
import ru.job4j.shortcut.model.Statistic;
import ru.job4j.shortcut.model.URL;
import ru.job4j.shortcut.repository.SiteRepository;
import ru.job4j.shortcut.repository.URLRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Service
public class SiteService {

    private final SiteRepository siteRepository;
    private final GeneratorService generatorService;
    private final BCryptPasswordEncoder encoder;
    private final URLRepository urlRepository;

    public SiteRegResponse registerSite(Site site) {
        Site siteFromDB = siteRepository.findBySite(site.getSite());
        if (siteFromDB != null) {
            return new SiteRegResponse("false", siteFromDB.getLogin(), "Сайт зарегистрирован ранее!");
        }
        String password = generatorService.generatePassword();
        site.setLogin(generatorService.generateLogin());
        site.setPassword(encoder.encode(password));
        Site newSite = siteRepository.save(site);
        return new SiteRegResponse("true", newSite.getLogin(), password);
    }

    public Site findByLogin(String login) {
        return siteRepository.findByLogin(login);
    }

    public List<Statistic> getStatistic() {
        String siteLogin = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Site site = siteRepository.findByLogin(siteLogin);
        if (site == null) {
            return Collections.emptyList();
        }
        Long siteId = site.getId();
        List<URL> urls = urlRepository.findAllBySiteId(siteId);
        List<Statistic> result = new ArrayList<>();
        for (URL url : urls) {
            result.add(new Statistic(url.getUrl(), url.getTotal()));
        }
        return result;
    }
}
