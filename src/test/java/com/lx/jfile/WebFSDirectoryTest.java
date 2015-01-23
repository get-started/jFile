package com.lx.jfile;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockServletContext;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by L.x on 15-1-23.
 */
public class WebFSDirectoryTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private MockServletContext context;

    @Before
    public void setUp() throws Exception {
        context = new MockServletContext("", new ResourceLoader() {
            @Override
            public Resource getResource(String path) {
                return new FileSystemResource(new File(folder.getRoot(), path));
            }

            @Override
            public ClassLoader getClassLoader() {
                return ClassLoader.getSystemClassLoader();
            }
        });
    }

    @Test
    public void pathStartsWithSlash() throws Exception {
        WebFSDirectory directory = new WebFSDirectory(context, "/upload");


        assertThat(directory.getPath(), equalTo("/upload"));
        assertThat(directory.getJavaFile(), equalTo(new File(folder.getRoot(), "upload")));
    }

    @Test
    public void pathNotStartWithSlash() throws Exception {
        WebFSDirectory directory = new WebFSDirectory(context, "upload");

        assertThat(directory.getPath(), equalTo("/upload"));
        assertThat(directory.getJavaFile(), equalTo(new File(folder.getRoot(), "upload")));
    }

    @Test
    public void pathEndsWithSlash() throws Exception {
        WebFSDirectory directory = new WebFSDirectory(context, "upload/");

        assertThat(directory.getPath(), equalTo("/upload"));
        assertThat(directory.getJavaFile(), equalTo(new File(folder.getRoot(), "upload")));
    }

    @Test
    public void pathAroundSlashes() throws Exception {
        WebFSDirectory directory = new WebFSDirectory(context, "/upload/");

        assertThat(directory.getPath(), equalTo("/upload"));
        assertThat(directory.getJavaFile(), equalTo(new File(folder.getRoot(), "upload")));
    }

    @Test
    public void includeContextPath() throws Exception {
        context.setContextPath("/app");
        WebFSDirectory directory = new WebFSDirectory(context, "/upload");

        assertThat(directory.getPath(), equalTo("/app/upload"));
    }
}
