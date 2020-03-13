package me.admin;

public class Utils {
    static String getBinFromCardNum(String cardNum) {
        return cardNum.substring(1, 6);
    }
}
