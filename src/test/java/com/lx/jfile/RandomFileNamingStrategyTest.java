package com.lx.jfile;

import com.lx.jfile.naming.RandomFileNamingStrategy;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by L.x on 15-1-23.
 */
public class RandomFileNamingStrategyTest {

    private RandomFileNamingStrategy namingStrategy;

    @Before
    public void setUp() throws Exception {
        namingStrategy = RandomFileNamingStrategy.getInstance();
    }

    @Test
    public void makeFilename() throws Exception {
        String originFileName = "test.png";

        String newFileName = namingStrategy.nameOf(originFileName);

        assertThat(newFileName, endsWith(".png"));
        assertThat(newFileName, not(originFileName));
    }


    @Test
    public void makeFileNameMissingExtension() throws Exception {
        String originFileName = "test";

        String newFileName = namingStrategy.nameOf(originFileName);

        assertThat(newFileName.indexOf('.'), equalTo(-1));
        assertThat(newFileName, not(originFileName));
    }

    @Test
    public void makeRandomFileName() throws Exception {
        Set<String> names = new HashSet<String>();
        for (any of : times) {
            String filename = namingStrategy.nameOf("test.png");
            assertThat(names, not(hasItem(filename)));
            names.add(filename);
        }
    }


    any[] times = new any[500];

    class any {
    }
}
