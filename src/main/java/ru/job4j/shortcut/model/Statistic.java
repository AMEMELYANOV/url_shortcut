package ru.job4j.shortcut.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Модель данных статистика
 *
 * @author Alexander Emelyanov
 * @version 1.0
 */
@Builder
@Data
@AllArgsConstructor
public class Statistic {

    /**
     * Наименование url ссылки
     */
    private String url;

    /**
     * Счетчик посещений
     */
    private int total;
}
