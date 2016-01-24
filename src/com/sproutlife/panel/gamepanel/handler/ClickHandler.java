package com.sproutlife.panel.gamepanel.handler;

import java.awt.event.MouseEvent;

import com.sproutlife.panel.gamepanel.handler.HandlerConsts.RequiredKey;

public abstract class ClickHandler extends Handler {
    public ClickHandler(RequiredKey requiredKey) {
        super(requiredKey);
    }
    
    public void mouseClicked(MouseEvent mouseEvent) { }
    public void mouseDoubleClicked(MouseEvent mouseEvent) { }
}
