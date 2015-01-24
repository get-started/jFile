package com.lx.jfile;

import com.lx.utils.JavaFile;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.mock.web.MockServletContext;

import javax.servlet.ServletContext;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static com.lx.utils.FileMatchers.content;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

/**
 * Created by L.x on 15-1-24.
 */
@RunWith(Parameterized.class)
public class ResourcesManagerTest {


    private String contextPath;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {""},
                {"/app"}
        });
    }

    public ResourcesManagerTest(String contextPath) {
        this.contextPath = contextPath;
    }

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    @Rule
    public ExpectedException exception = ExpectedException.none();

    private ResourcesManager resourcesManager;
    private FSDirectory uploadDir;
    private FSDirectory webRootDir;

    @Before
    public void setUp() throws Exception {
        final File webRoot = folder.newFolder("webRoot");
        ServletContext servletContext = new MockServletContext(new RelativeResourceLoader(webRoot)) {{
            setContextPath(contextPath);
        }};
        resourcesManager = new ResourcesManager();
        uploadDir = new WebFSDirectory(servletContext, "upload");
        webRootDir = new WebFSDirectory(servletContext, "");
        resourcesManager.setDirectories(Arrays.asList(webRootDir, uploadDir));

        assertTrue(uploadDir.exists());
        assertTrue(webRootDir.exists());
    }

    @Test
    public void directory() throws Exception {
        assertThat(resourcesManager.directory("/upload"), equalTo(uploadDir));
        assertThat(resourcesManager.directory(""), equalTo(webRootDir));
    }

    @Test
    public void shouldRaisingExceptionIfDirectoryNotFound() throws Exception {
        exception.expect(IOException.class);

        resourcesManager.directory("unknown");
    }

    @Test
    public void file() throws Exception {
        assertThat(resourcesManager.file("/upload/test.txt"), equalTo(uploadDir.file("test.txt")));
        assertThat(resourcesManager.file("test.txt"), equalTo(webRootDir.file("test.txt")));
    }

    @Test
    public void saveFile() throws Exception {
        FSFile file = resourcesManager.save(JavaFile.file("test.txt"), "");

        assertTrue(webRootDir.exists(file));
        assertThat(file.getJavaFile(), content("simple"));
    }

    @Test
    public void saveStream() throws Exception {
        FSFile file = resourcesManager.save("test.txt", new ByteArrayInputStream("simple".getBytes()), "");

        assertTrue(webRootDir.exists(file));
        assertThat(file.getJavaFile(), content("simple"));

    }

    @Test
    public void moveFile() throws Exception {
        FSFile srcFile = webRootDir.file("test.txt");
        srcFile.create();

        FSFile destFile = resourcesManager.move("test.txt", "/upload");

        assertFalse(srcFile.exists());
        assertTrue(destFile.exists());
        assertThat(uploadDir.file("test.txt"), equalTo(destFile));
    }

    @Test
    public void shouldMovingNonExistingFileRaisingException() throws Exception {
        exception.expect(IOException.class);

        resourcesManager.move("unknown", "/upload");
    }

    @Test
    public void skipMovingFileBetweenSameDirectory() throws Exception {
        FSFile srcFile = webRootDir.file("test.txt");
        srcFile.create();

        FSFile file = resourcesManager.move("test.txt", "");

        assertThat(file, equalTo(srcFile));
    }
}
