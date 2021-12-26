package ru.job4j.shortcut.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SiteRegResponse {
    private String registration;
    private String login;
    private String password;
}
