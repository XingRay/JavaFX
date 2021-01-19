package com.xingray.javafx.page;

import com.xingray.javafx.base.BaseController;

import java.util.HashMap;
import java.util.Map;

public class Router {

    private Map<String, Class> classMap;

    public Router() {
        classMap = new HashMap<>();
    }

    public void init(Class<? extends BaseController>... classes) {
        for (Class<? extends BaseController> cls : classes) {
            register(cls);
        }
    }

    private void register(Class<? extends BaseController> cls) {
        RoutePath routePathAnnotation = cls.getAnnotation(RoutePath.class);
        if (routePathAnnotation == null) {
            return;
        }
        String routePath = routePathAnnotation.value();
        classMap.put(routePath, cls);
    }
}
