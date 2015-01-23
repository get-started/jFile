package com.lx.jfile;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.lx.utils.FileMatchers.content;
import static com.lx.utils.JavaFile.file;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

/**
 * Created by L.x on 15-1-23.
 */
public class FSFileTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    @Rule
    public ExpectedException exception = ExpectedException.none();

    private File destJavaFile;
    private FSFile file;
    private FSDirectory directory;
    private FSDirectory other;

    @Before
    public void setUp() throws Exception {
        destJavaFile = folder.newFile();
        directory = new FSDirectory(folder.getRoot());
        file = new FSFile(directory, destJavaFile);
        other = new FSDirectory(folder.newFolder("other"));
    }

    @Test
    public void shouldCreateWithAnDirectoryRaisingException() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(folder.getRoot().getAbsolutePath() + " is not a File");

        new FSFile(directory, folder.getRoot());
    }

    @Test
    public void getPath() throws Exception {
        FSFile file = new FSFile(directory, new File(folder.getRoot(), "test"));

        assertThat(file.getPath(), equalTo(directory.getPath() + "/test"));
    }

    @Test
    public void create() throws Exception {
        FSFile file = new FSFile(directory, new File(folder.getRoot(), "unknown"));
        assertFalse(file.exists());

        file.create();

        assertTrue(file.exists());
    }

    @Test
    public void saveFromJavaFile() throws Exception {
        file.save(file("test.txt"));

        assertThat(destJavaFile, content("simple"));
    }

    @Test
    public void saveFromStream() throws Exception {
        String content = "simple";

        file.save(new ByteArrayInputStream(content.getBytes()));

        assertThat(destJavaFile, content(content));
    }

    @Test
    public void closingStreamAfterSave() throws Exception {
        final AtomicBoolean closed = new AtomicBoolean(false);

        file.save(new ByteArrayInputStream(new byte[0]) {
            @Override
            public void close() throws IOException {
                closed.set(true);
            }
        });

        assertTrue(closed.get());
    }

    @Test
    public void delete() throws Exception {
        file.delete();

        assertFalse(file.exists());
    }

    @Test
    public void moveFileToAnotherDirectory() throws Exception {
        FSFile target = file.move(other);

        assertThat(target.getPath(), equalTo(other.getPath() + "/" + destJavaFile.getName()));
        assertFalse(file.exists());
        assertTrue(target.exists());
    }

    @Test
    public void shouldMovingFileToTheSameDirectoryRaisingException() throws Exception {
        exception.expect(IOException.class);

        file.move(directory);
    }

    @Test
    public void shouldMovingNonExistingFileRaisingException() throws Exception {
        FSFile file = new FSFile(directory, new File(folder.getRoot(), "unknown"));

        exception.expect(IOException.class);

        file.move(other);
    }
}
