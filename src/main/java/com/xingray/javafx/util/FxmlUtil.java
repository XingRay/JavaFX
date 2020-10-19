package com.xingray.javafx.util;


import com.xingray.javafx.base.BaseController;
import com.xingray.javafx.base.StageHolder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class FxmlUtil {

    public static <T extends BaseController> StageHolder<T> loadFxml(String path) {
        return loadFxml(path, new Stage(), new FXMLLoader());
    }

    public static <T extends BaseController> StageHolder<T> loadFxml(String path, Stage stage) {
        return loadFxml(path, stage, new FXMLLoader());
    }

    public static <T extends BaseController> StageHolder<T> loadFxml(String path, Stage stage, FXMLLoader fxmlLoader) {
        URL url = null;
        try {
            url = new URL(path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return loadFxml(url, stage, fxmlLoader);
    }

    public static <T extends BaseController> StageHolder<T> loadFxml(URL url) {
        return loadFxml(url, new Stage(), new FXMLLoader());
    }

    public static <T extends BaseController> StageHolder<T> loadFxml(URL url, Stage stage) {
        return loadFxml(url, stage, new FXMLLoader());
    }

    public static <T extends BaseController> StageHolder<T> loadFxml(URL url, Stage stage, FXMLLoader fxmlLoader) {
        fxmlLoader.setLocation(url);
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (root == null) {
            return null;
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
