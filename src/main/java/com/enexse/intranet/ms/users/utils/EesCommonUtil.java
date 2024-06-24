package com.enexse.intranet.ms.users.utils;

import com.enexse.intranet.ms.users.constants.EesUserConstants;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class EesCommonUtil {

    public static String generateCurrentDateUtil() {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(EesUserConstants.EES_PATTERN_DATE);
        return dtf.format(LocalDateTime.now());
    }

    public static String generateExpirationLink() {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(EesUserConstants.EES_PATTERN_DATE);
        return dtf.format(LocalDateTime.now().plusMinutes(EesUserConstants.EES_EXPIRATION_LINK));
    }

    /*public static String generateTemporalPassword() {
        return RandomStringUtils.random(1, EesUserConstants.EES_RANDOM_LETTERS).toUpperCase()
                + RandomStringUtils.random(6, EesUserConstants.EES_RANDOM_LETTERS).toLowerCase()
                + RandomStringUtils.random(5, EesUserConstants.EES_RANDOM_NUMBERS)
                + RandomStringUtils.random(1, EesUserConstants.EES_RANDOM_CHARS);
    }*/

    public static String generateCodeTwoFactoryAuth() {
        return RandomStringUtils.random(6, EesUserConstants.EES_RANDOM_NUMBERS);
    }

    public static String generateCapitalize(String name) {
        return name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1).toLowerCase(Locale.ROOT);
    }

    public static String generateFirstPassword(String lastname) {

        String firstPassword = lastname.split(" ")[0].substring(0, 1).toUpperCase(Locale.ROOT)
                + lastname.split(" ")[0].substring(1).toLowerCase(Locale.ROOT)
                + "@1234";
        return firstPassword;
    }

    public static String[] generateDefaultAvatar() {
        int rnd = new Random().nextInt(EesUserConstants.EES_DEFAULT_AVATAR.length);
        return EesUserConstants.EES_DEFAULT_AVATAR[rnd];
    }

    public static String generateDefaultDepartmentColor() {
        int rnd = new Random().nextInt(EesUserConstants.EES_DEFAULT_DEPARTMENT_COLOR.length);
        return EesUserConstants.EES_DEFAULT_DEPARTMENT_COLOR[rnd];
    }
}
