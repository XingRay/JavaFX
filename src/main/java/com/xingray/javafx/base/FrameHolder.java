package com.xingray.javafx.base;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FrameHolder<T> {
    private final T controller;
    private final Stage stage;
    private final Scene scene;
    private final Parent frame;

    public FrameHolder(T controller, Stage stage, Scene scene, Parent frame) {
        this.controller = controller;
        this.stage = stage;
        this.scene = scene;
        this.frame = frame;
    }

    public T getController() {
        return controller;
    }

    public Stage getStage() {
        return stage;
    }

    public Scene getScene() {
        return scene;
    }

    public Parent getFrame() {
        return frame;
    }

    @Override
    public String toString() {
        return "FrameHolder{" +
                "controller=" + controller +
                ", stage=" + stage +
                ", scene=" + scene +
                ", frame=" + frame +
                '}';
    }
}
