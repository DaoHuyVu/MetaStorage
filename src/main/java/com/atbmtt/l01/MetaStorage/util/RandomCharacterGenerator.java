package com.atbmtt.l01.MetaStorage.util;

import java.util.Random;

public class RandomCharacterGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new Random();
    public static String getString(byte length){
        StringBuilder builder = new StringBuilder();
        for(byte i = 0 ;i<length ;++i){
            int index = (RANDOM.nextInt(CHARACTERS.length()));
            builder.append(CHARACTERS.charAt(index));
        }
        return builder.toString();
    }
}
