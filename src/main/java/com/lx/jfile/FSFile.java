package com.lx.jfile;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.apache.commons.io.FileUtils.copyInputStreamToFile;
import static org.apache.commons.io.FileUtils.openInputStream;
import static org.apache.commons.io.IOUtils.closeQuietly;

/**
 * Created by L.x on 15-1-23.
 */
public class FSFile extends FSEntry {
    private FSDirectory directory;


    public FSFile(FSDirectory directory, File target) {
        super(target);
        if (target.isDirectory()) {
            throw new IllegalArgumentException(target.getAbsolutePath() + " is not a File");
        }
        this.directory = directory;
    }

    public InputStream getInputStream() throws IOException {
        return openInputStream(target);
    }

    public void save(File source) throws IOException {
        save(openInputStream(source));
    }

    public void save(InputStream content) throws IOException {
        try {
            copyInputStreamToFile(content, target);
        } finally {
            closeQuietly(content);
        }
    }

    public String getPath() {
        return directory.getPath() + "/" + target.getName();
    }

    public void create() throws IOException {
        FileUtils.touch(target);
    }

    public FSFile move(FSDirectory dest) throws IOException {
        FSFile destFile = dest.save(target);
        delete();
        return destFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        FSFile fsFile = (FSFile) o;

        return !(directory != null ? !directory.equals(fsFile.directory) : fsFile.directory != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (directory != null ? directory.hashCode() : 0);
        return result;
    }

    boolean in(FSDirectory directory) {
        return this.directory.equals(directory);
    }
}
