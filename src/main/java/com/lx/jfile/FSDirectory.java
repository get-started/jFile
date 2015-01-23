package com.lx.jfile;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.apache.commons.io.FileUtils.openInputStream;

/**
 * Created by L.x on 15-1-23.
 */
public class FSDirectory extends FSEntry {

    public FSDirectory(File target) {
        super(target);
        if (target.isFile()) {
            throw new IllegalArgumentException(target.getAbsolutePath() + " is not a Directory!");
        }

    }


    @Override
    public void create() throws IOException {
        FileUtils.forceMkdir(target);
    }

    public FSFile save(File file) throws IOException {
        return save(file.getName(), openInputStream(file));
    }

    public FSFile save(String filename, InputStream content) throws IOException {
        FSFile file = new FSFile(this, new File(target, filename));
        file.save(content);
        return file;
    }

    @Override
    public String getPath() {
        return target.getAbsolutePath().replace('\\', '/');
    }


}
