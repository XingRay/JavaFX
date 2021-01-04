package com.xingray.javafx.config.fieldconverters;

import com.xingray.config.FieldConverter;
import javafx.scene.control.TextInputControl;

public class TextInputControlConverter implements FieldConverter<TextInputControl, String> {

    @Override
    public String getConfig(TextInputControl textInputControl) {
        return textInputControl.getText();
    }

    @Override
    public void restoreConfig(TextInputControl textInputControl, String s) {
        textInputControl.setText(s);
    }
}
