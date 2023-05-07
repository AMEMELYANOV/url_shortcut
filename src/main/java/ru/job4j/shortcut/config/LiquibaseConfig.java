package ru.job4j.shortcut.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Конфигурация средства миграции
 *
 * @author Alexander Emelyanov
 * @version 1.0
 */
@Configuration
public class LiquibaseConfig {

    /**
     * Создание объекта миграции приложения,
     * параметры считываются из файла /resources/liquibase-changeLog.xml.
     *
     * @param dataSource источник данных
     * @return объект миграции данных
     */
    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:liquibase-changeLog.xml");
        liquibase.setDataSource(dataSource);
        return liquibase;
    }

}
