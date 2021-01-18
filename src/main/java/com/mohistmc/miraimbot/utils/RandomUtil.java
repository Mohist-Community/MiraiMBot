package com.mohistmc.miraimbot.utils;

import java.util.Random;

public class RandomUtil {

    public static String random(int i) {
        String[] c = "qwertyuiopasdfghjklzxcvbnm1234567890QWERTYUIOPLKJHGFDSAZXCVBNM".split("");
        StringBuilder x = new StringBuilder();
        Random r = new Random();
        for (int q = 0; q < i; q++)
            x.append(c[r.nextInt(c.length)]);
        return x.toString();
    }

}
