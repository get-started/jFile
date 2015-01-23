package com.lx.utils;

import java.io.File;
import java.net.URISyntaxException;

import static java.lang.ClassLoader.getSystemResource;

/**
 * Created by L.x on 15-1-23.
 */
public class JavaFile {
    public static File file(String name) throws URISyntaxException {
        return new File(getSystemResource(name).toURI());
    }
}
