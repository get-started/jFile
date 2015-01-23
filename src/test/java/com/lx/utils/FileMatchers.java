package com.lx.utils;

import org.apache.commons.io.FileUtils;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Created by L.x on 15-1-23.
 */
public class FileMatchers {
    public static Matcher<? super File> content(String content) {
        return content(equalTo(content));
    }

    public static Matcher<? super File> content(Matcher<String> content) {
        return new FeatureMatcher<File, String>(content, "content", "content") {
            @Override
            protected String featureValueOf(File file) {
                try {
                    return FileUtils.readFileToString(file);
                } catch (IOException e) {
                    throw new AssertionError(e);
                }
            }
        };
    }
}
