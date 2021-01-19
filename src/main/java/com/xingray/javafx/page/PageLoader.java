package com.xingray.javafx.page;


import com.xingray.javafx.base.BaseController;
import com.xingray.javafx.base.BaseStage;
import com.xingray.javafx.base.FrameHolder;
import com.xingray.javafx.base.StageHolder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Function;

public class PageLoader {

    private String rootPath;
    private Function<String, URL> urlMapper;

    public PageLoader() {
    }

    public PageLoader(String rootPath) {
        this.rootPath = rootPath;
    }

    public PageLoader(Function<String, URL> urlMapper) {
        this.urlMapper = urlMapper;
    }

    private URL getUrl(String path) {
        URL url = null;

        if (urlMapper != null) {
            return urlMapper.apply(path);
        }

        try {
            if (rootPath != null) {
                path = rootPath + path;
            }
            url = new URL(path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getLayoutPath(Class<?> cls) {
        LayoutPath layoutPathAnnotation = cls.getAnnotation(LayoutPath.class);
        if (layoutPathAnnotation == null) {
            throw new IllegalArgumentException("class:" + cls.getName() + " is annotationed by " + LayoutPath.class.getName());
        }
        return layoutPathAnnotation.value();
    }

    public <T extends BaseController> StageHolder<T> loadFxml(String path) {
        return loadFxml(path, new BaseStage(), new FXMLLoader());
    }

    public <T extends BaseController> StageHolder<T> loadFxml(String path, BaseStage stage) {
        return loadFxml(path, stage, new FXMLLoader());
    }

    public <T extends BaseController> StageHolder<T> loadFxml(String path, BaseStage stage, FXMLLoader fxmlLoader) {
        return loadFxml(getUrl(path), stage, fxmlLoader);
    }

    public <T extends BaseController> StageHolder<T> loadFxml(URL url) {
        return loadFxml(url, new BaseStage(), new FXMLLoader());
    }

    public <T extends BaseController> StageHolder<T> loadFxml(URL url, BaseStage stage) {
        return loadFxml(url, stage, new FXMLLoader());
    }

    public <T extends BaseController> StageHolder<T> loadFxml(URL url, BaseStage stage, FXMLLoader fxmlLoader) {
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
            controller.create();
        }
        return new StageHolder<>(controller, stage, scene);
    }

    public <T extends BaseController> StageHolder<T> loadFxml(Class<T> cls, BaseStage stage, FXMLLoader fxmlLoader) {
        return loadFxml(getLayoutPath(cls), stage, fxmlLoader);
    }

    public <T extends BaseController> StageHolder<T> loadFxml(Class<T> cls, BaseStage stage) {
        return loadFxml(cls, stage, new FXMLLoader());
    }

    public <T extends BaseController> StageHolder<T> loadFxml(Class<T> cls) {
        return loadFxml(cls, new BaseStage(), new FXMLLoader());
    }

    public <T extends BaseController> FrameHolder<T> loadFrame(BaseStage stage, Pane root, Class<T> cls) {
        return loadFrame(stage, root, cls, null);
    }

    public <T extends BaseController> FrameHolder<T> loadFrame(BaseStage stage, Pane root, Class<T> cls, Object... args) {
        URL resource = getUrl(getLayoutPath(cls));
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(resource);
        Parent frame = null;
        try {
            frame = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (frame == null) {
            return null;
        }

        root.getChildren().clear();
        root.getChildren().add(frame);
        if (root instanceof AnchorPane) {
            AnchorPane.setTopAnchor(frame, 0.0);
            AnchorPane.setBottomAnchor(frame, 0.0);
            AnchorPane.setLeftAnchor(frame, 0.0);
            AnchorPane.setRightAnchor(frame, 0.0);
        }

        T controller = fxmlLoader.getController();
        if (controller != null) {
            controller.setScene(root.getScene());
            controller.setStage(stage);
            controller.setPageParams(args);

            controller.create();
        }

        return new FrameHolder<>(controller, stage, root.getScene());
    }
}
