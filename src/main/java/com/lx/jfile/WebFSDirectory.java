package com.lx.jfile;

import javax.servlet.ServletContext;
import java.io.File;

/**
 * Created by L.x on 15-1-23.
 */
public class WebFSDirectory extends FSDirectory {
    private final String contextPath;
    private String path;

    public WebFSDirectory(ServletContext context, String path) {
        super(new File(context.getRealPath(path)));
        contextPath = context.getContextPath();
        this.path = optimize(path);
    }

    private String optimize(String path) {
        String result = path.replaceAll("^/|/$", "");
        if (result.equals("")) {
            return result;
        }
        return "/" + result;
    }

    @Override
    public String getPath() {
        return contextPath + path;
    }

    public boolean match(String path) {
        return this.path.equals(path);
    }
}
