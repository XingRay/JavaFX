package com.xingray.javafx.config;

public interface ViewState<T, S> {
    S getViewState(T view);

    void setViewState(T view, S state);
}
