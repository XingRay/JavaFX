package com.xingray.javafx.config;

public interface ViewState {
    String getViewState(Object view);

    void setViewState(Object view, String state);
}
