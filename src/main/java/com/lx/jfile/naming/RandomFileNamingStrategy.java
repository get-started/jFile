package com.lx.jfile.naming;

import org.apache.commons.io.FilenameUtils;

import java.util.UUID;

/**
 * Created by L.x on 15-1-23.
 */
public class RandomFileNamingStrategy implements FileNamingStrategy {

    private static final RandomFileNamingStrategy INSTANCE = new RandomFileNamingStrategy();

    public static RandomFileNamingStrategy getInstance() {
        return INSTANCE;
    }

    private RandomFileNamingStrategy() {
    }

    public String nameOf(String filename) {
        return UUID.randomUUID() + extensionOf(filename);
    }

    private String extensionOf(String filename) {
        String ext = FilenameUtils.getExtension(filename);
        return ext.isEmpty() ? "" : "." + ext;
    }
}
