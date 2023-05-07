package ru.job4j.shortcut.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static ru.job4j.shortcut.filter.JWTAuthenticationFilter.*;

/**
 * Фильтр запросов с JWT токеном
 *
 * @author Alexander Emelyanov
 * @version 1.0
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    /**
     * Конструктор
     *
     * @param authenticationManager менеджер аутентификации
     */
    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * Проверяет наличие JWT токена в хедерах запроса, если успешно,
     * устанавливает аутентификацию в контекст безопасности.
     *
     * @param request запрос пользователя
     * @param response ответ пользователю
     * @param chain цепь фильтров
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(HEADER_STRING);
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    /**
     * Создает пользовательский объект на основании JWT токена
     * и возвращает его.
     *
     * @param request запрос пользователя
     * @return объект аутентификации
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""))
                    .getSubject();
            if (user != null) {
                return new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}