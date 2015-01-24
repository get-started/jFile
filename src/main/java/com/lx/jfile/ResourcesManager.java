package com.lx.jfile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.apache.commons.io.FilenameUtils.getFullPathNoEndSeparator;
import static org.apache.commons.io.FilenameUtils.getName;

/**
 * Created by L.x on 15-1-24.
 */
public class ResourcesManager {
    private List<FSDirectory> directories;

    public void setDirectories(List<FSDirectory> directories) throws IOException {
        for (FSDirectory directory : directories) {
            directory.create();
        }
        this.directories = directories;
    }

    public FSDirectory directory(String path) throws IOException {
        for (FSDirectory directory : directories) {
            if (directory.match(path)) {
                return directory;
            }
        }
        throw new IOException("Directory not found:" + path + " !");
    }

    public FSFile file(String path) throws IOException {
        return directory(getFullPathNoEndSeparator(path)).file(getName(path));
    }

    public FSFile save(File source, String path) throws IOException {
        return directory(path).save(source);
    }

    public FSFile save(String filename, InputStream content, String path) throws IOException {
        return directory(path).save(filename, content);
    }

    public FSFile move(String filename, String destinationDir) throws IOException {
        FSFile sourceFile = file(filename);
        FSDirectory directory = directory(destinationDir);
        if (sourceFile.in(directory)) {
            return sourceFile;
        }
        return sourceFile.move(directory);
    }
}
