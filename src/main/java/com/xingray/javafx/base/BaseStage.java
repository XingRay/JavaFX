package com.xingray.javafx.base;

import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.util.LinkedList;
import java.util.List;

public class BaseStage extends Stage {

    private final List<EventHandler<WindowEvent>> onCloseEventHandlers = new LinkedList<>();

    public BaseStage() {
        initOnCloseEventHandler();
    }

    public BaseStage(StageStyle style) {
        super(style);
        initOnCloseEventHandler();
    }

    public void addOnCloseEventHandler(EventHandler<WindowEvent> eventEventHandler) {
        onCloseEventHandlers.add(eventEventHandler);
    }

    private void initOnCloseEventHandler() {
        super.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                List<EventHandler<WindowEvent>> handlers = onCloseEventHandlers;
                if (handlers == null || handlers.isEmpty()) {
                    return;
                }
                for (EventHandler<WindowEvent> eventHandler : handlers) {
                    eventHandler.handle(event);
                }
            }
        });
    }
}
