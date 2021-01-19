package com.xingray.javafx.page;

public class PageUtil {
    public static String getLayoutPath(Class<?> cls) {
        LayoutPath layoutPathAnnotation = cls.getAnnotation(LayoutPath.class);
        if (layoutPathAnnotation == null) {
            throw new IllegalArgumentException("class:" + cls.getName() + " is annotationed by " + LayoutPath.class.getName());
        }
        return layoutPathAnnotation.value();
    }
}
