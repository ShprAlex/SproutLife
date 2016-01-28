/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.panel.gamepanel.handler;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;

import com.sproutlife.panel.PanelController;
import com.sproutlife.panel.gamepanel.handler.HandlerConsts.RequiredKey;

public class ScrollDragHandler extends BackgroundDragHandler {
    private PanelController gc;
    private Point lastDragPoint;
    //private static Cursor handCursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
    private Cursor lastCursor;
    
    public ScrollDragHandler(PanelController panelController, RequiredKey requiredKey) {
        super(requiredKey);
        this.gc = panelController;
    }
    
    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        lastDragPoint = mouseEvent.getPoint();
        lastCursor = gc.getScrollPanel().getCursor();
        gc.getScrollPanel().setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    @Override
    public void mouseReleased(MouseEvent mouseEvent, boolean mouseOverChanged) {
        lastDragPoint = null;
        gc.getScrollPanel().setCursor(lastCursor);
    }
    
    @Override
    public void mouseDragged(MouseEvent mouseEvent, boolean mouseOverChanged) {
        int deltaX = lastDragPoint.x - mouseEvent.getPoint().x;
        int deltaY = lastDragPoint.y - mouseEvent.getPoint().y;
        lastDragPoint = mouseEvent.getPoint();
        gc.getScrollController().scrollBy(deltaX, deltaY);
    }
}
