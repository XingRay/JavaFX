package com.xingray.javafx.config;

import com.xingray.javafx.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ViewConfig<T> {

    private final Object controller;
    private final Class<T> configClass;
    private Map<String, Field> controllerViewFields;
    private final ViewState viewState;

    public ViewConfig(Object controller, Class<T> configClass, ViewState viewState) {
        this.controller = controller;
        this.configClass = configClass;
        this.viewState = viewState;

        init();
    }

    private void init() {
        controllerViewFields = new HashMap<>();
        Field[] fields = controller.getClass().getFields();
        for (Field field : fields) {
            ConfigKey configKeyAnnotation = field.getAnnotation(ConfigKey.class);
            if (configKeyAnnotation == null) {
                continue;
            }
            String configKey = configKeyAnnotation.value();
            controllerViewFields.put(configKey, field);
        }
    }

    public T getConfig() {
        try {
            T config = configClass.getConstructor().newInstance();
            populateConfig(config);
            return config;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void populateConfig(T config) {
        if (config == null) {
            return;
        }

        Field[] declaredFields = config.getClass().getDeclaredFields();
        Set<String> configPropertyNames = new HashSet<>();
        for (Field field : declaredFields) {
            configPropertyNames.add(field.getName());
        }

        configPropertyNames.retainAll(controllerViewFields.keySet());
        for (String name : configPropertyNames) {
            Field field = controllerViewFields.get(name);
            try {
                Object viewField = field.get(controller);
                String value = viewState.getViewState(viewField);
                ReflectUtil.set(config, name, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void populateView(T config) {
        if (config == null) {
            return;
        }

        Field[] declaredFields = config.getClass().getDeclaredFields();
        Set<String> configPropertyNames = new HashSet<>();
        for (Field field : declaredFields) {
            configPropertyNames.add(field.getName());
        }

        configPropertyNames.retainAll(controllerViewFields.keySet());
        for (String name : configPropertyNames) {
            Field field = controllerViewFields.get(name);
            try {
                Object viewField = field.get(controller);
                String value = (String) ReflectUtil.get(config, name);
                viewState.setViewState(viewField, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
