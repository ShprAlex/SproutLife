package com.sproutlife.panel.gamepanel.handler;

import java.awt.event.MouseEvent;

import com.sproutlife.panel.gamepanel.handler.HandlerConsts.RequiredKey;

public abstract class BackgroundDragHandler extends Handler {
    public BackgroundDragHandler(RequiredKey requiredKey) {
        super(requiredKey);
    }
    
    public void mousePressed(MouseEvent mouseEvent) { }
    public void mouseReleased(MouseEvent mouseEvent, boolean mouseOverStable) { }
    public void mouseDragged(MouseEvent mouseEvent, boolean mouseOverStable) { }
}
