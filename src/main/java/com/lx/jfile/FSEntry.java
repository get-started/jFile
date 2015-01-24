package com.lx.jfile;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by L.x on 15-1-23.
 */
abstract class FSEntry {
    protected File target;

    public FSEntry(File target) {
        this.target = target;
    }

    public boolean exists() {
        return target.exists();
    }

    public void delete() throws IOException {
        FileUtils.forceDelete(target);
    }

    public File getJavaFile() {
        return target;
    }

    public String getName() {
        return target.getName();
    }

    public abstract void create() throws IOException;

    public abstract String getPath();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FSEntry fsEntry = (FSEntry) o;

        return !(target != null ? !target.equals(fsEntry.target) : fsEntry.target != null);

    }

    @Override
    public int hashCode() {
        return target != null ? target.hashCode() : 0;
    }

    @Override
    public String toString() {
        return target.toString();
    }
}
