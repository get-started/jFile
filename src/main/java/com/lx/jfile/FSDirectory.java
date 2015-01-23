package com.lx.jfile;

import com.lx.jfile.naming.FileNamingStrategy;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static com.lx.jfile.naming.NonNamingStrategy.getInstance;
import static org.apache.commons.io.FileUtils.openInputStream;

/**
 * Created by L.x on 15-1-23.
 */
public class FSDirectory extends FSEntry {

    private FileNamingStrategy fileNamingStrategy= getInstance();

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
        FSFile file = new FSFile(this, new File(target, fileNamingStrategy.nameOf(filename)));
        file.save(content);
        return file;
    }

    @Override
    public String getPath() {
        return target.getAbsolutePath().replace('\\', '/');
    }


    public void setFileNamingStrategy(FileNamingStrategy fileNamingStrategy) {
        this.fileNamingStrategy = fileNamingStrategy;
    }
}
