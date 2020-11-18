package com.xingray.javafx.config.viewstates;

import com.xingray.javafx.config.ViewState;
import javafx.scene.control.TextInputControl;

public class TextInputControlViewState implements ViewState<TextInputControl, String> {

    @Override
    public String getViewState(TextInputControl view) {
        return view.getText();
    }

    @Override
    public void setViewState(TextInputControl view, String state) {
        view.setText(state);
    }
}
