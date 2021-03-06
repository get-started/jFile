package com.lx.jfile;

import com.lx.jfile.naming.FileNamingStrategy;
import com.lx.utils.JavaFile;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static com.lx.utils.FileMatchers.content;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

/**
 * Created by L.x on 15-1-23.
 */
public class FSDirectoryTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File destFile;
    private File nonExistingFolder;
    private File rootFolder;

    @Before
    public void setUp() throws Exception {
        rootFolder = folder.newFolder("root");
        destFile = new File(rootFolder, "test.txt");
        nonExistingFolder = new File(rootFolder, "unknown");
    }


    @Test
    public void getPath() throws Exception {
        FSDirectory directory = new FSDirectory(rootFolder);

        assertThat(directory.getPath(), equalTo(rootFolder.getAbsolutePath().replace('\\', '/')));
    }

    @Test
    public void createWithFileRaisingException() throws Exception {
        File file = folder.newFile();

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(file.getAbsolutePath() + " is not a Directory!");

        new FSDirectory(file);
    }

    @Test
    public void mkdirs() throws Exception {
        FSDirectory directory = new FSDirectory(nonExistingFolder);
        assertFalse(directory.exists());

        directory.create();

        assertTrue(directory.exists());
    }

    @Test
    public void delete() throws Exception {
        FSDirectory directory = new FSDirectory(rootFolder);

        directory.delete();

        assertFalse(directory.exists());
    }

    @Test
    public void openFile() throws Exception {
        FSDirectory directory = new FSDirectory(rootFolder);

        FSFile file = directory.file("test.txt");

        file.create();
        assertTrue(new File(directory.getJavaFile(), "test.txt").exists());
    }

    @Test
    public void saveFile() throws Exception {
        destFile.delete();
        FSDirectory directory = new FSDirectory(rootFolder);

        FSFile file = directory.save(JavaFile.file("test.txt"));

        assertThat(IOUtils.toString(file.getInputStream()), equalTo("simple"));
        assertThat(destFile, content("simple"));
    }


    @Test
    public void savingFileToNonExistingDirectory() throws Exception {
        FSDirectory directory = new FSDirectory(nonExistingFolder);

        directory.save(JavaFile.file("test.txt"));

        assertTrue(directory.exists());
        assertThat(new File(nonExistingFolder, "test.txt"), content("simple"));
    }

    @Test
    public void enableFileNameStrategy() throws Exception {
        FSDirectory directory = new FSDirectory(rootFolder);
        directory.setFileNamingStrategy(new FileNamingStrategy() {
            public String nameOf(String filename) {
                return "fixed";
            }
        });

        FSFile file = directory.save(JavaFile.file("test.txt"));
        assertThat(file.getName(), equalTo("fixed"));
    }
}
