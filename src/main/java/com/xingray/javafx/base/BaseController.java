package com.xingray.javafx.base;

import com.xingray.javafx.config.AutoConfig;
import com.xingray.javafx.config.fieldconverters.FieldConverters;
import com.xingray.util.TaskExecutor;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextInputControl;
import javafx.stage.WindowEvent;

public abstract class BaseController {

    protected BaseStage stage;
    protected Scene scene;
    private AutoConfig autoConfig;

    public void initialize() {
    }

    public void create() {
        stage.addOnCloseEventHandler(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                destroy();
            }
        });

        onCreated();
    }

    public void onCreated() {

    }

    private void destroy() {
        if (autoConfig != null) {
            autoConfig.save();
        }
        onDestroy();
    }

    public void onDestroy() {

    }

    public void enableAutoConfig() {
        autoConfig = new AutoConfig(this, TaskExecutor.ioPool(), TaskExecutor.uiPool());
        autoConfig.addFieldConverter(TextInputControl.class, FieldConverters.textInputControlConverter);
        autoConfig.addFieldConverter(DatePicker.class, FieldConverters.datePickerConverter);
        autoConfig.addFieldConverter(ChoiceBox.class, FieldConverters.choiceBoxConverter);
        autoConfig.addFieldConverter(CheckBox.class, FieldConverters.checkBoxConverter);
        autoConfig.restore();
    }


    public BaseStage getStage() {
        return stage;
    }

    public void setStage(BaseStage stage) {
        this.stage = stage;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
