package com.xingray.javafx.util;


import com.xingray.javafx.base.BaseController;
import com.xingray.javafx.base.StageHolder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class FxmlUtil {

    public static <T extends BaseController> StageHolder<T> loadFxml(String fxml) {
        return loadFxml(fxml, new Stage(), new FXMLLoader());
    }

    public static <T extends BaseController> StageHolder<T> loadFxml(String fxml, Stage stage) {
        return loadFxml(fxml, stage, new FXMLLoader());
    }

    public static <T extends BaseController> StageHolder<T> loadFxml(String fxml, Stage stage, FXMLLoader fxmlLoader) {
        URL url = FxmlUtil.class.getResource(fxml);
        fxmlLoader.setLocation(url);
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (root instanceof Region) {
            Region region = (Region) root;
            double minHeight = region.getMinHeight();
            if (minHeight >= 0) {
                stage.setMinHeight(minHeight);
            }
            double minWidth = region.getMinWidth();
            if (minWidth >= 0) {
                stage.setMinWidth(minWidth);
            }
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        T controller = fxmlLoader.getController();
        if (controller != null) {
            controller.setScene(scene);
            controller.setStage(stage);
            controller.onCreated();
        }
        return new StageHolder<>(controller, stage, scene);
    }
}
