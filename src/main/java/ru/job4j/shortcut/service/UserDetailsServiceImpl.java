package ru.job4j.shortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.shortcut.model.Site;

import static java.util.Collections.emptyList;

/**
 * Реализация сервиса по работе с данными пользователя
 *
 * @see org.springframework.security.core.userdetails.UserDetailsService
 * @author Alexander Emelyanov
 * @version 1.0
 */
@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * Объект для доступа к методам SiteService
     */
    private final SiteService siteService;

    /**
     * Выполняет поиск и возврат пользователя/сайта.
     *
     * @param username имя пользователя
     * @return пользователь
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Site site = siteService.findByLogin(username);
        if (site == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(site.getLogin(), site.getPassword(), emptyList());
    }
}
