package com.xingray.javafx.base;

import com.xingray.javafx.config.AutoConfig;
import com.xingray.javafx.config.fieldconverters.FieldConverters;
import com.xingray.javafx.page.PageTask;
import com.xingray.javafx.page.PageUtil;
import com.xingray.javafx.page.RouteUtil;
import com.xingray.util.TaskExecutor;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class Controller {

    protected BaseStage stage;
    protected Scene scene;
    private AutoConfig autoConfig;
    private Map<String, PageTask> pageRouteMap;
    private Controller parent;
    private Object[] pageParams;

    private Function<String, URL> urlMapper;

    public void initialize() {
    }

    public void create() {
        stage.addOnCloseEventHandler(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                destroy();
            }
        });
        pageRouteMap = new HashMap<>();

        onCreated();
    }

    public void onCreated() {

    }

    private void destroy() {
        if (autoConfig != null) {
            autoConfig.save();
        }
        onDestroy();
    }

    public void onDestroy() {

    }

    public void enableAutoConfig() {
        autoConfig = new AutoConfig(this, TaskExecutor.ioPool(), TaskExecutor.uiPool());
        autoConfig.addFieldConverter(TextInputControl.class, FieldConverters.textInputControlConverter);
        autoConfig.addFieldConverter(DatePicker.class, FieldConverters.datePickerConverter);
        autoConfig.addFieldConverter(ChoiceBox.class, FieldConverters.choiceBoxConverter);
        autoConfig.addFieldConverter(CheckBox.class, FieldConverters.checkBoxConverter);
        autoConfig.restore();
    }


    public BaseStage getStage() {
        return stage;
    }

    public void setStage(BaseStage stage) {
        this.stage = stage;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Controller getParent() {
        return parent;
    }

    public void setParent(Controller parent) {
        this.parent = parent;
    }

    public Object[] getPageParams() {
        return pageParams;
    }

    public void setPageParams(Object[] pageParams) {
        this.pageParams = pageParams;
    }

    public void setUrlMapper(Function<String, URL> urlMapper) {
        this.urlMapper = urlMapper;
    }

    public boolean gotoPage(Class<? extends Controller> cls) {
        return gotoPage(cls, null);
    }

    public boolean gotoPage(Class<? extends Controller> cls, Object... args) {
        PageTask pageTask = pageRouteMap.get(RouteUtil.getRoutePath(cls));
        if (pageTask != null) {
            pageTask.open(args);
            return true;
        }

        if (parent != null) {
            if (parent.gotoPage(cls, args)) {
                return true;
            }
        }

        if (urlMapper != null) {
            StageHolder<? extends Controller> holder = loadFxml(cls);
            holder.getController().setPageParams(args);
            holder.getStage().show();
            return true;
        }

        return false;
    }

    public void addPageRoute(Class<? extends Controller> cls, PageTask task) {
        String routePath = RouteUtil.getRoutePath(cls);
        if (routePath == null) {
            return;
        }
        pageRouteMap.put(routePath, task);
    }

    private URL getUrl(String path) {
        return urlMapper.apply(path);
    }

    public <T extends Controller> StageHolder<T> loadFxml(String path) {
        return loadFxml(path, new BaseStage(), new FXMLLoader());
    }

    public <T extends Controller> StageHolder<T> loadFxml(String path, BaseStage stage) {
        return loadFxml(path, stage, new FXMLLoader());
    }

    public <T extends Controller> StageHolder<T> loadFxml(String path, BaseStage stage, FXMLLoader fxmlLoader) {
        return loadFxml(getUrl(path), stage, fxmlLoader);
    }

    public <T extends Controller> StageHolder<T> loadFxml(URL url) {
        return loadFxml(url, new BaseStage(), new FXMLLoader());
    }

    public <T extends Controller> StageHolder<T> loadFxml(URL url, BaseStage stage) {
        return loadFxml(url, stage, new FXMLLoader());
    }

    public <T extends Controller> StageHolder<T> loadFxml(URL url, BaseStage stage, FXMLLoader fxmlLoader) {
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
            controller.setParent(this);

            controller.create();
        }
        return new StageHolder<>(controller, stage, scene);
    }

    public <T extends Controller> StageHolder<T> loadFxml(Class<T> cls, BaseStage stage, FXMLLoader fxmlLoader) {
        return loadFxml(PageUtil.getLayoutPath(cls), stage, fxmlLoader);
    }

    public <T extends Controller> StageHolder<T> loadFxml(Class<T> cls, BaseStage stage) {
        return loadFxml(cls, stage, new FXMLLoader());
    }

    public <T extends Controller> StageHolder<T> loadFxml(Class<T> cls) {
        return loadFxml(cls, new BaseStage(), new FXMLLoader());
    }

    public <T extends Controller> FrameHolder<T> loadFrame(BaseStage stage, Pane root, Class<T> cls) {
        return loadFrame(stage, root, cls, (Object[]) null);
    }

    public <T extends Controller> FrameHolder<T> loadFrame(BaseStage stage, Pane root, Class<T> cls, Object... args) {
        URL resource = getUrl(PageUtil.getLayoutPath(cls));
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
            controller.setParent(this);

            controller.create();
        }

        return new FrameHolder<>(controller, stage, root.getScene());
    }
}
