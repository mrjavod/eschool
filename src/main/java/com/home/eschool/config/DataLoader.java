package com.home.eschool.config;

import com.home.eschool.entity.Roles;
import com.home.eschool.entity.States;
import com.home.eschool.services.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;
    private final StateService stateService;
    private final LanguageService languageService;
    private final AppSettingsService appSettingsService;

    public DataLoader(UserService userService,
                      RoleService roleService,
                      StateService stateService,
                      LanguageService languageService,
                      AppSettingsService appSettingsService) {
        this.userService = userService;
        this.roleService = roleService;
        this.stateService = stateService;
        this.languageService = languageService;
        this.appSettingsService = appSettingsService;
    }

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String status;

    @Override
    public void run(String... args) {

        List<Roles> roles = roleService.generateRoles();

        List<States> states = stateService.generateDefaultStates();

        languageService.generateLanguages();

        appSettingsService.createDefaultSettings();

        userService.createDefaultUser(roles, states);
    }
}
