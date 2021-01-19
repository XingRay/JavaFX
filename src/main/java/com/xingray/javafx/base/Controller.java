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
    private Map<Pane, Map<Class<? extends Controller>, FrameHolder>> frameHolderMap;
    private Map<Pane, FrameHolder> currentFrameHolderMap;

    private Function<String, URL> urlMapper;

    public void initialize() {
    }

    public void create() {
        stage.addOnCloseEventHandler(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                hide();
                destroy();
            }
        });
        pageRouteMap = new HashMap<>();
        frameHolderMap = new HashMap<>();
        currentFrameHolderMap = new HashMap<>();

        onCreated();
    }

    protected void show() {
        onShow();
    }

    protected void hide() {
        onHide();
    }

    private void destroy() {
        if (autoConfig != null) {
            autoConfig.save();
        }
        onDestroy();
    }

    public void onCreated() {

    }

    public void onShow() {

    }

    public void onCommand(Object[] args) {

    }

    public void onHide() {

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
            holder.getStage().show();
            holder.getController().show();
            holder.getController().onCommand(args);
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
        return loadFxml(getUrl(path), new BaseStage(), new FXMLLoader());
    }

    public <T extends Controller> StageHolder<T> loadFxml(String path, BaseStage stage) {
        return loadFxml(getUrl(path), stage, new FXMLLoader());
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

    public <T extends Controller> StageHolder<T> loadFxml(Class<T> cls, BaseStage stage, FXMLLoader fxmlLoader) {
        return loadFxml(getUrl(PageUtil.getLayoutPath(cls)), stage, fxmlLoader);
    }

    public <T extends Controller> StageHolder<T> loadFxml(Class<T> cls, BaseStage stage) {
        return loadFxml(getUrl(PageUtil.getLayoutPath(cls)), stage, new FXMLLoader());
    }

    public <T extends Controller> StageHolder<T> loadFxml(Class<T> cls) {
        return loadFxml(getUrl(PageUtil.getLayoutPath(cls)), new BaseStage(), new FXMLLoader());
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

    public <T extends Controller> StageHolder<T> openPage(String path) {
        return openPage(getUrl(path), new BaseStage(), new FXMLLoader(), (Object[]) null);
    }

    public <T extends Controller> StageHolder<T> openPage(String path, BaseStage stage) {
        return openPage(getUrl(path), stage, new FXMLLoader(), (Object[]) null);
    }

    public <T extends Controller> StageHolder<T> openPage(String path, BaseStage stage, FXMLLoader fxmlLoader) {
        return openPage(getUrl(path), stage, fxmlLoader, (Object[]) null);
    }

    public <T extends Controller> StageHolder<T> openPage(URL url) {
        return openPage(url, new BaseStage(), new FXMLLoader(), (Object[]) null);
    }

    public <T extends Controller> StageHolder<T> openPage(URL url, BaseStage stage) {
        return openPage(url, stage, new FXMLLoader(), (Object[]) null);
    }

    public <T extends Controller> StageHolder<T> openPage(Class<T> cls, BaseStage stage, FXMLLoader fxmlLoader) {
        return openPage(getUrl(PageUtil.getLayoutPath(cls)), stage, fxmlLoader, (Object[]) null);
    }

    public <T extends Controller> StageHolder<T> openPage(Class<T> cls, BaseStage stage, Object... args) {
        return openPage(getUrl(PageUtil.getLayoutPath(cls)), stage, new FXMLLoader(), args);
    }

    public <T extends Controller> StageHolder<T> openPage(Class<T> cls, BaseStage stage) {
        return openPage(getUrl(PageUtil.getLayoutPath(cls)), stage, new FXMLLoader(), (Object[]) null);
    }

    public <T extends Controller> StageHolder<T> openPage(Class<T> cls, Object... args) {
        return openPage(getUrl(PageUtil.getLayoutPath(cls)), new BaseStage(), new FXMLLoader(), args);
    }

    public <T extends Controller> StageHolder<T> openPage(Class<T> cls) {
        return openPage(getUrl(PageUtil.getLayoutPath(cls)), new BaseStage(), new FXMLLoader(), (Object[]) null);
    }

    public <T extends Controller> StageHolder<T> openPage(URL url, BaseStage stage, FXMLLoader fxmlLoader) {
        return openPage(url, stage, fxmlLoader, (Object[]) null);
    }

    public <T extends Controller> StageHolder<T> openPage(URL url, BaseStage stage, FXMLLoader fxmlLoader, Object... args) {
        StageHolder<T> holder = loadFxml(url, stage, fxmlLoader);
        holder.getStage().show();
        holder.getController().show();
        if (args != null) {
            holder.getController().onCommand(args);
        }
        return holder;
    }

    public <T extends Controller> FrameHolder<T> loadFrame(Pane root, Class<T> cls) {
        return loadFrame(getStage(), root, cls, (Object[]) null);
    }

    public <T extends Controller> FrameHolder<T> loadFrame(Pane root, Class<T> cls, Object... args) {
        return loadFrame(getStage(), root, cls, args);
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
            controller.setParent(this);

            controller.create();
            controller.show();
            if (args != null) {
                controller.onCommand(args);
            }
        }

        return new FrameHolder<>(controller, stage, root.getScene(), frame);
    }

    public <T extends Controller> FrameHolder<T> switchFrame(Pane root, Class<T> cls) {
        return switchFrame(getStage(), root, cls, (Object[]) null);
    }

    public <T extends Controller> FrameHolder<T> switchFrame(Pane root, Class<T> cls, Object... args) {
        return switchFrame(getStage(), root, cls, args);
    }

    public <T extends Controller> FrameHolder<T> switchFrame(BaseStage stage, Pane root, Class<T> cls) {
        return switchFrame(stage, root, cls, (Object[]) null);
    }

    public <T extends Controller> FrameHolder<T> switchFrame(BaseStage stage, Pane root, Class<T> cls, Object... args) {
        Map<Class<? extends Controller>, FrameHolder> frameHolderMap = this.frameHolderMap.get(root);
        if (frameHolderMap == null) {
            frameHolderMap = new HashMap<>();
            this.frameHolderMap.put(root, frameHolderMap);
        }
        FrameHolder<T> oldFrameHolder = currentFrameHolderMap.get(root);

        FrameHolder<T> newFrameHolder = frameHolderMap.get(cls);
        if (oldFrameHolder != null && newFrameHolder == oldFrameHolder) {
            return oldFrameHolder;
        }
        if (oldFrameHolder != null) {
            oldFrameHolder.getController().hide();
        }
        if (newFrameHolder != null) {
            root.getChildren().clear();
            root.getChildren().add(newFrameHolder.getFrame());
            newFrameHolder.getController().show();
            if (args != null) {
                newFrameHolder.getController().onCommand(args);
            }

            currentFrameHolderMap.put(root, newFrameHolder);
            return newFrameHolder;
        }

        newFrameHolder = loadFrame(stage, root, cls, args);
        if (newFrameHolder != null) {
            frameHolderMap.put(cls, newFrameHolder);
            currentFrameHolderMap.put(root, newFrameHolder);
        }

        return newFrameHolder;
    }
}
