package com.xingray.javafx.config.fieldconverters;

import com.xingray.config.FieldConverter;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;

public class ChoiceBoxConverter implements FieldConverter<ChoiceBox, String> {
    @Override
    public String getConfig(ChoiceBox choiceBox) {
        StringConverter converter = choiceBox.getConverter();
        Object value = choiceBox.getValue();
        if (converter != null) {
            return converter.toString(value);
        }
        return value.toString();
    }

    @Override
    public void restoreConfig(ChoiceBox choiceBox, String s) {
        StringConverter converter = choiceBox.getConverter();
        ObservableList items = choiceBox.getItems();
        if (items == null || items.isEmpty()) {
            choiceBox.getSelectionModel().select(0);
            return;
        }
        if (converter != null) {
            Object o = converter.fromString(s);
            if (o == null) {
                choiceBox.getSelectionModel().select(0);
                return;
            }
            int index = items.indexOf(o);
            if (index < 0) {
                index = 0;
            }
            choiceBox.getSelectionModel().select(index);
            return;
        }

        for (Object o : items) {
            String itemString = o == null ? "" : o.toString();
            if (itemString.equals(s)) {
                int index = items.indexOf(o);
                if (index < 0) {
                    continue;
                }
                choiceBox.getSelectionModel().select(index);
                return;
            }
        }
        choiceBox.getSelectionModel().select(0);
    }
}
