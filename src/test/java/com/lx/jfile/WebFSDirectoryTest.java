package com.lx.jfile;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.mock.web.MockServletContext;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by L.x on 15-1-23.
 */
@RunWith(Parameterized.class)
public class WebFSDirectoryTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private MockServletContext servletContext;
    private String contextPath;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {""},
                {"/app"}
        });
    }

    public WebFSDirectoryTest(String contextPath) {
        this.contextPath = contextPath;
    }

    @Before
    public void setUp() throws Exception {
        servletContext = new MockServletContext(new RelativeResourceLoader(folder.getRoot()));
        servletContext.setContextPath(contextPath);
    }

    @Test
    public void webRoot() throws Exception {
        WebFSDirectory directory = new WebFSDirectory(servletContext, "");

        assertThat(directory.getPath(), equalTo(servletContext.getContextPath()));
    }

    private String path(String path) {
        return servletContext.getContextPath() + path;
    }

    @Test
    public void pathStartsWithSlash() throws Exception {
        WebFSDirectory directory = new WebFSDirectory(servletContext, "/upload");


        assertThat(directory.getPath(), equalTo(path("/upload")));
        assertTrue(directory.match("/upload"));
    }

    @Test
    public void pathNotStartWithSlash() throws Exception {
        WebFSDirectory directory = new WebFSDirectory(servletContext, "upload");

        assertThat(directory.getPath(), equalTo(path("/upload")));
    }

    @Test
    public void pathEndsWithSlash() throws Exception {
        WebFSDirectory directory = new WebFSDirectory(servletContext, "upload/");

        assertThat(directory.getPath(), equalTo(path("/upload")));
    }

    @Test
    public void pathAroundSlashes() throws Exception {
        WebFSDirectory directory = new WebFSDirectory(servletContext, "/upload/");

        assertThat(directory.getPath(), equalTo(path("/upload")));
    }

}
