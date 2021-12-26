package ru.job4j.shortcut.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "urls")
public class URL {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private String code;
    private Long siteId;
    private int total;
}
