package com.xingray.javafx.config;

import com.xingray.javafx.config.viewstates.DatePickerViewState;
import com.xingray.javafx.config.viewstates.TextInputControlViewState;
import com.xingray.javafx.util.ReflectUtil;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextInputControl;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ViewConfig<T> {

    private final Object controller;
    private final Class<T> configClass;
    private Map<String, Field> controllerViewFields;
    private final Map<Class, ViewState> viewStates;

    public ViewConfig(Object controller, Class<T> configClass) {
        this.controller = controller;
        this.configClass = configClass;
        this.viewStates = new HashMap<>();

        init();
    }

    public <V, S> void addViewState(Class<V> viewCls, ViewState<V, S> viewState) {
        viewStates.put(viewCls, viewState);
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

        addViewState(TextInputControl.class, new TextInputControlViewState());
        addViewState(DatePicker.class, new DatePickerViewState());
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
                ViewState viewState = getViewState(viewField);
                if (viewState != null) {
                    Object value = viewState.getViewState(viewField);
                    ReflectUtil.set(config, name, value);
                } else {
                    Object value = viewField;
                    ReflectUtil.set(config, name, value);
                }
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
                Object value = ReflectUtil.get(config, name);
                ViewState viewState = getViewState(viewField);
                if (viewState != null) {
                    viewState.setViewState(viewField, value);
                } else {
                    field.set(controller, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private ViewState getViewState(Object viewField) {
        ViewState viewState = viewStates.get(viewField.getClass());
        if (viewState == null) {
            for (Map.Entry<Class, ViewState> entry : viewStates.entrySet()) {
                Class cls = entry.getKey();
                if (cls.isInstance(viewField)) {
                    viewState = entry.getValue();
                    if (viewState != null) {
                        viewStates.put(cls, viewState);
                    }
                    break;
                }
            }
        }
        return viewState;
    }
}
