package com.xingray.javafx.base;

import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class BaseController {

    protected Stage stage;
    protected Scene scene;

    public void initialize() {
    }

    public void onCreated() {
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }


}
