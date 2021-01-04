module JavaFX {
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;
    requires Config;
    requires Util;

    exports com.xingray.javafx.base;
    exports com.xingray.javafx.page;
    exports com.xingray.javafx.config;
    exports com.xingray.javafx.config.fieldconverters;
}