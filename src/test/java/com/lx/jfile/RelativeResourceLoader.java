package com.lx.jfile;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;

/**
* Created by L.x on 15-1-24.
*/
class RelativeResourceLoader implements ResourceLoader {
    private final File root;

    public RelativeResourceLoader(File root) {
        this.root = root;
    }

    @Override
    public Resource getResource(String path) {
        return new FileSystemResource(new File(root, path));
    }

    @Override
    public ClassLoader getClassLoader() {
        return ClassLoader.getSystemClassLoader();
    }
}
