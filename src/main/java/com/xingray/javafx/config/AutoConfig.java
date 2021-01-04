package com.xingray.javafx.config;

import com.xingray.config.FieldConverter;
import com.xingray.config.MapConfig;
import com.xingray.util.ConfigUtil;
import com.xingray.util.StringUtil;
import com.xingray.util.SystemUtil;

import java.io.File;
import java.util.Map;
import java.util.concurrent.Executor;

public class AutoConfig {

    private final MapConfig mapConfig;
    private final Executor ioExecutor;
    private final Executor uiExecutor;
    private String path;

    public AutoConfig(Object controller, Executor ioExecutor, Executor uiExecutor) {
        mapConfig = new MapConfig(controller);
        this.ioExecutor = ioExecutor;
        this.uiExecutor = uiExecutor;

        String name = controller.getClass().getCanonicalName();
        String[] names = name.split("\\.");
        setPath(SystemUtil.getUserDirectory() + File.separator + "config" + File.separator + StringUtil.toString(names, File.separator) + ".properties");
    }

    public void setPath(String path) {
        this.path = path;
    }

    public <T> void addFieldConverter(Class<T> cls, FieldConverter<T, String> fieldConverter) {
        mapConfig.addFieldConverter(cls, fieldConverter);
    }

    public void save() {
        Map<String, String> configMap = mapConfig.getConfig();
        ioExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ConfigUtil.saveConfig(path, configMap);
            }
        });
    }

    public void restore() {
        ioExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, String> configMap = ConfigUtil.loadConfig(path);
                uiExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        mapConfig.updateTarget(configMap);
                    }
                });
            }
        });
    }
}
