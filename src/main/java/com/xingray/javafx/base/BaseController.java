package com.xingray.javafx.base;

import com.xingray.javafx.config.AutoConfig;
import com.xingray.javafx.config.fieldconverters.FieldConverters;
import com.xingray.javafx.page.PageLoader;
import com.xingray.javafx.page.PageTask;
import com.xingray.javafx.page.RouteUtil;
import com.xingray.util.TaskExecutor;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextInputControl;
import javafx.stage.WindowEvent;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseController {

    protected BaseStage stage;
    protected Scene scene;
    private AutoConfig autoConfig;
    private Map<String, PageTask> pageRouteMap;
    private BaseController parent;
    private PageLoader pageLoader;
    private Object[] pageParams;

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

    public BaseController getParent() {
        return parent;
    }

    public void setParent(BaseController parent) {
        this.parent = parent;
    }

    public PageLoader getPageLoader() {
        return pageLoader;
    }

    public void setPageLoader(PageLoader pageLoader) {
        this.pageLoader = pageLoader;
    }

    public Object[] getPageParams() {
        return pageParams;
    }

    public void setPageParams(Object[] pageParams) {
        this.pageParams = pageParams;
    }

    public boolean gotoPage(Class<? extends BaseController> cls) {
        return gotoPage(cls, null);
    }

    public boolean gotoPage(Class<? extends BaseController> cls, Object... args) {
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

        if (pageLoader != null) {
            StageHolder<? extends BaseController> holder = pageLoader.loadFxml(cls);
            holder.getController().setPageParams(args);
            holder.getStage().show();
            return true;
        }

        return false;
    }

    public void addPageRoute(Class<? extends BaseController> cls, PageTask task) {
        pageRouteMap.put(RouteUtil.getRoutePath(cls), task);
    }
}
