package com.home.eschool.repository;

import com.home.eschool.entity.Languages;
import com.home.eschool.entity.enums.LangEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LanguagesRepo extends JpaRepository<Languages, UUID> {

    Languages getLanguagesByLabel(LangEnum label);
}
