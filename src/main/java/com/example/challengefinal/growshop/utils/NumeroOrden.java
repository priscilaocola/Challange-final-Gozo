package com.example.challengefinal.growshop.utils;

import java.util.Random;

public final class NumeroOrden {

    private NumeroOrden(){}

    public static Random randomNumber = new Random();

    public static String getRandomNum() {
        int randomNum = randomNumber.nextInt(9000000);
        String formatRandomNum = "Gozo" + String.format("%04d", randomNum);
        return formatRandomNum;
    }
}
