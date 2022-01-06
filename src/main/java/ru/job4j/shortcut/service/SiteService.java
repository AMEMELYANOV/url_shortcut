package ru.job4j.shortcut.service;

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
import java.util.List;

@Service
public class SiteService {
    private final SiteRepository siteRepository;
    private final GeneratorService generatorService;
    private final BCryptPasswordEncoder encoder;
    private final URLRepository urlRepository;

    public SiteService(SiteRepository siteRepository, GeneratorService generatorService,
                       BCryptPasswordEncoder encoder, URLRepository urlRepository) {
        this.siteRepository = siteRepository;
        this.generatorService = generatorService;
        this.encoder = encoder;
        this.urlRepository = urlRepository;
    }


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
        Long siteId = siteRepository.findByLogin(siteLogin).getId();
        List<URL> urls = urlRepository.findAllBySiteId(siteId);
        List<Statistic> result = new ArrayList<>();
        for (URL url : urls) {
            result.add(new Statistic(url.getUrl(), url.getTotal()));
        }
        return result;
    }
}
