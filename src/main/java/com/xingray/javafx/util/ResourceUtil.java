package com.xingray.javafx.util;

import java.net.URL;

public class ResourceUtil {

    public static String loadCss(String name) {
        URL resource = ResourceUtil.class.getResource("/css/" + name + ".css");
        if (resource == null) {
            throw new NullPointerException("resource is null");
        }
        return resource.toExternalForm();
    }

    public static String getAbsolutePath(String relativePath) {
        String userDir = System.getProperty("user.dir");
        if (userDir == null) {
            URL url = ResourceUtil.class.getResource(relativePath);
            return url.getPath();
        }

        return userDir + relativePath;
    }
}
