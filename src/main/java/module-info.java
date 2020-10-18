module JavaFX {
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.xingray.javafx.base;
    exports com.xingray.javafx.base;

    opens com.xingray.javafx.util;
    exports com.xingray.javafx.util;
}