package ru.job4j.shortcut.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sites")
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String site;
    private String login;
    private String password;
}
