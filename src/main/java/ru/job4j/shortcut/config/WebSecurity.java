package ru.job4j.shortcut.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.job4j.shortcut.filter.JWTAuthenticationFilter;
import ru.job4j.shortcut.filter.JWTAuthorizationFilter;
import ru.job4j.shortcut.service.UserDetailsServiceImpl;

import static ru.job4j.shortcut.filter.JWTAuthenticationFilter.*;

/**
 * Конфигурация системы безопасности
 *
 * @author Alexander Emelyanov
 * @version 1.0
 */
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurity {

    /**
     * Объект для доступа к методам UserDetailsServiceImpl
     */
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Шифратор паролей
     */
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Создает конфигурацию авторизации при работе с приложением.
     *
     * @param http объект HttpSecurity для которого выполняется настройка авторизации
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL, LOGIN).permitAll()
                .antMatchers(HttpMethod.GET, REDIRECT).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(
                        http.getSharedObject(AuthenticationConfiguration.class))))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(
                        http.getSharedObject(AuthenticationConfiguration.class))))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }

    /**
     * Создание менеджера аутентификации.
     *
     * @param authConf конфигурация аутентификации
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConf)
            throws Exception {
        return authConf.getAuthenticationManager();
    }

    /**
     * Создание объекта конфигурации CORS фильтра для Spring Security
     *
     * @return конфигурация CORS фильтра
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}