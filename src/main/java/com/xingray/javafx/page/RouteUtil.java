package com.xingray.javafx.page;

public class RouteUtil {

    public static String getRoutePath(Class<?> cls){
        RoutePath routePathAnnotation = cls.getAnnotation(RoutePath.class);
        if (routePathAnnotation == null) {
            return null;
        }
        return routePathAnnotation.value();
    }
}
