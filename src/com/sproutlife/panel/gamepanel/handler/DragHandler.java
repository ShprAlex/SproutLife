package com.sproutlife.panel.gamepanel.handler;

import java.awt.event.MouseEvent;

import com.sproutlife.panel.gamepanel.handler.HandlerConsts.RequiredKey;

public abstract class DragHandler extends Handler {
    public DragHandler(RequiredKey requiredKey) {
        super(requiredKey);
    }
    
    public void mousePressed(MouseEvent mouseEvent) { }
    public void mouseReleased(MouseEvent mouseEvent) { }
    public void mouseDragged(MouseEvent mouseEvent) { }
}
