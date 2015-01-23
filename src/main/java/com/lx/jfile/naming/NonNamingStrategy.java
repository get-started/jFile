package com.lx.jfile.naming;

/**
 * Created by L.x on 15-1-23.
 */
public class NonNamingStrategy implements FileNamingStrategy {

    private static final NonNamingStrategy INSTANCE = new NonNamingStrategy();

    public static NonNamingStrategy getInstance() {
        return INSTANCE;
    }

    private NonNamingStrategy() {
    }

    @Override
    public String nameOf(String filename) {
        return filename;
    }
}
