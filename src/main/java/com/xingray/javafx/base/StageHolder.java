package com.xingray.javafx.base;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class StageHolder<T extends Controller> {
    private final T controller;
    private final Stage stage;
    private final Scene scene;

    public StageHolder(T controller, Stage stage, Scene scene) {
        this.controller = controller;
        this.stage = stage;
        this.scene = scene;
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

    @Override
    public String toString() {
        return "StageHolder{" +
                "controller=" + controller +
                ", stage=" + stage +
                ", scene=" + scene +
                '}';
    }
}
