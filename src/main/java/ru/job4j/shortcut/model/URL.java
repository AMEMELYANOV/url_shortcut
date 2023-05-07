package ru.job4j.shortcut.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Модель данных url ссылка
 *
 * @author Alexander Emelyanov
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "urls")
public class URL {

    /**
     * Идентификатор url ссылки
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Наименование url ссылки
     */
    private String url;

    /**
     * Код
     */
    private String code;

    /**
     * Идентификатор сайта
     */
    private Long siteId;

    /**
     * Счетчик посещений
     */
    private int total;
}
