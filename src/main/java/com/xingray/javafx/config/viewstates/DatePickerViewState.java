package com.xingray.javafx.config.viewstates;

import com.xingray.javafx.config.ViewState;
import javafx.scene.control.DatePicker;

public class DatePickerViewState implements ViewState<DatePicker, String> {
    @Override
    public String getViewState(DatePicker view) {
        return view.getEditor().getText();
    }

    @Override
    public void setViewState(DatePicker view, String state) {
        view.getEditor().setText(state);
    }
}
