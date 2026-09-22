package ru.netology.data;

public class DataHelper {

    private DataHelper() {
    }

    public static String getApprovedCardNumber() {
        return "4444 4444 4444 4441";
    }

    public static String getDeclinedCardNumber() {
        return "4444 4444 4444 4442";
    }

    public static String getValidMonth() {
        return "09";
    }

    public static String getValidYear() {
        return "27";
    }

    public static String getValidHolder() {
        return "IVAN IVANOV";
    }

    public static String getValidCvc() {
        return "123";
    }
}