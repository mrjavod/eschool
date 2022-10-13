package com.home.eschool.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.regex.Pattern;

public class Utils {

    public static String getCondLike(int size) {
        return size > 0 ? " or " : " where ";
    }

    public static String getCond(int size) {
        return size > 0 ? " and " : " where ";
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isEmpty(Boolean b) {
        return b == null;
    }

    public static boolean isEmpty(Double db) {
        return db == null || db.equals(Double.NaN);
    }

    public static boolean isEmpty(java.sql.Date dt) {
        return dt == null;
    }

    public static boolean isEmpty(java.util.Date dt) {
        return dt == null;
    }

    public static java.sql.Date d2sql(java.util.Date dt) {
        return new java.sql.Date(dt.getTime());
    }

    public static boolean isEmpty(Object last_change) {
        return last_change == null;
    }

    public static boolean isEmpty(String[] array_of_strings) {
        return array_of_strings == null || array_of_strings.length <= 0;
    }

    public static boolean isNumeric(String strNum) {

        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

        if (strNum == null) {
            return false;
        }

        return pattern.matcher(strNum).matches();
    }

    public static String convertToString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object convertToObject(String str) {
        try {
            if (str == null)
                return null;

            return new ObjectMapper().readValue(str, new TypeReference<Object>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return str;
        }
    }

    public static <T> T convertToObject(String str, Class<T> tClass) {
        try {
            if (str == null)
                return null;

            return new ObjectMapper().readValue(str, new TypeReference<T>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String inputStreamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    public static String getPstr(Exception ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    public static Object convertToNumeric(String number) {

        if (number.contains(".")) {
            return Double.valueOf(number);
        }

        return Integer.valueOf(number);
    }

}
