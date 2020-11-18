package com.xingray.javafx.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class ReflectUtil {

    /**
     * 根据属性，获取get方法
     *
     * @param fieldName 属性名
     */
    public static Method getGetter(Class<?> cls, String fieldName) {
        Field field = null;
        try {
            field = cls.getDeclaredField(fieldName);
        } catch (NoSuchFieldException ignored) {

        }
        if (field == null) {
            return null;
        }

        Class<?> type = field.getType();
        String methodName = getGetterName(fieldName, type);

        try {
            return cls.getMethod(methodName);
        } catch (NoSuchMethodException ignored) {

        }
        return null;
    }

    public static String getGetterName(String fieldName, Class<?> type) {
        String methodName;
        if (type == boolean.class) {
            if (isStartWithIsField(fieldName)) {
                methodName = fieldName;
            } else {
                methodName = "is" + StringUtil.captain(fieldName);
            }
        } else if (type == Boolean.class) {
            if (isStartWithIsField(fieldName)) {
                methodName = "get" + StringUtil.captain(fieldName.substring(2));
            } else {
                methodName = "get" + StringUtil.captain(fieldName);
            }
        } else {
            methodName = "get" + StringUtil.captain(fieldName);
        }

        return methodName;
    }

    public static boolean isStartWithIsField(String fieldName) {
        return fieldName.startsWith("is") && fieldName.length() > 2 && Character.isUpperCase(fieldName.charAt(2));
    }

    /**
     * 根据属性，获取set方法
     */
    public static Method getSetter(Class<?> cls, String fieldName) {
        Field field = null;
        try {
            field = cls.getDeclaredField(fieldName);
        } catch (NoSuchFieldException ignored) {

        }
        if (field == null) {
            return null;
        }

        Class<?> type = field.getType();
        String methodName = getSetterName(fieldName, type);

        try {
            return cls.getMethod(methodName, type);
        } catch (NoSuchMethodException ignored) {

        }
        return null;
    }

    public static String getSetterName(String fieldName, Class<?> type) {
        String methodName;
        if (type == boolean.class) {
            if (isStartWithIsField(fieldName)) {
                methodName = "set" + StringUtil.captain(fieldName.substring(2));
            } else {
                methodName = "set" + StringUtil.captain(fieldName);
            }
        } else if (type == Boolean.class) {
            if (isStartWithIsField(fieldName)) {
                methodName = "set" + StringUtil.captain(fieldName.substring(2));
            } else {
                methodName = "set" + StringUtil.captain(fieldName);
            }
        } else {
            methodName = "set" + StringUtil.captain(fieldName);
        }
        return methodName;
    }


    public static Object get(Object o, String fieldName) {
        return get(o, fieldName, null);
    }

    public static Object get(Object o, String fieldName, Map<String, Method> getterCache) {
        Method getter = null;
        if (getterCache != null && !getterCache.isEmpty()) {
            getter = getterCache.get(fieldName);
        }
        if (getter == null) {
            getter = getGetter(o.getClass(), fieldName);
            if (getter != null && getterCache != null) {
                getterCache.put(fieldName, getter);
            }
        }
        if (getter == null) {
            return null;
        }
        try {
            return getter.invoke(o);
        } catch (IllegalAccessException | InvocationTargetException ignored) {

        }
        return null;
    }

    public static void set(Object o, String fieldName, Object value) {
        set(o, fieldName, value, null);
    }

    public static void set(Object o, String fieldName, Object value, Map<String, Method> setterCache) {
        Method setter = null;
        if (setterCache != null && !setterCache.isEmpty()) {
            setter = setterCache.get(fieldName);
        }
        if (setter == null) {
            setter = getSetter(o.getClass(), fieldName);
            if (setter != null && setterCache != null) {
                setterCache.put(fieldName, setter);
            }
        }
        if (setter == null) {
            return;
        }
        try {
            Class<?> paramClass = setter.getParameterTypes()[0];
            setter.invoke(o, ObjectUtil.ensureMatchesType(value, paramClass));
        } catch (IllegalAccessException | InvocationTargetException ignored) {

        }
    }
}
