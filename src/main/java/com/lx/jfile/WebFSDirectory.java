package com.lx.jfile;

import javax.servlet.ServletContext;
import java.io.File;

/**
 * Created by L.x on 15-1-23.
 */
public class WebFSDirectory extends FSDirectory {
    private String path;

    public WebFSDirectory(ServletContext context, String path) {
        super(new File(context.getRealPath(path)));
        this.path = context.getContextPath() + optimize(path);
    }

    private String optimize(String path) {
        if(path.equals("")){
            return path;
        }
        return "/" + path.replaceAll("^/|/$", "");
    }

    @Override
    public String getPath() {
        return path;
    }
}
