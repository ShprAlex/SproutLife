package com.sproutlife.panel.gamepanel.handler;

import java.awt.event.MouseWheelEvent;

import com.sproutlife.panel.gamepanel.handler.HandlerConsts.RequiredKey;

public abstract class MouseWheelHandler extends Handler {
    public MouseWheelHandler(RequiredKey requiredKey) {
        super(requiredKey);
    }
    
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) { }
}
