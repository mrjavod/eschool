package com.home.eschool.services;

import com.home.eschool.entity.Languages;
import com.home.eschool.entity.enums.LangEnum;
import com.home.eschool.repository.LanguagesRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LanguageService {

    private final LanguagesRepo languagesRepo;

    public LanguageService(LanguagesRepo languagesRepo) {
        this.languagesRepo = languagesRepo;
    }

    public void generateLanguages() {
        if (languagesRepo.count() == 0) {
            List<Languages> list = new ArrayList<>();
            list.add(new Languages(UUID.randomUUID(), "Eng", LangEnum.EN));
            list.add(new Languages(UUID.randomUUID(), "Rus", LangEnum.RU));
            list.add(new Languages(UUID.randomUUID(), "Uzb", LangEnum.UZ));

            languagesRepo.saveAll(list);
        }
    }

    public List<Languages> getLanguageList() {
        return languagesRepo.findAll();
    }

    public Languages getLanguageByLabel(LangEnum label) {
        return languagesRepo.getLanguagesByLabel(label);
    }
}
