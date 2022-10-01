package com.home.eschool.utils;

import com.home.eschool.entity.Users;
import com.home.eschool.entity.enums.LangEnum;

public class Settings {

    private static Users currentUser;
    private static LangEnum lang;

    public static Users getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(Users currentUser) {
        Settings.currentUser = currentUser;
    }

    public static LangEnum getLang() {
        return lang;
    }

    public static void setLang(LangEnum lang) {
        Settings.lang = lang;
    }
}
