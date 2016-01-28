/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.panel.gamepanel.handler;

import java.awt.event.MouseWheelEvent;

import com.sproutlife.panel.gamepanel.handler.HandlerConsts.RequiredKey;

public abstract class MouseWheelHandler extends Handler {
    public MouseWheelHandler(RequiredKey requiredKey) {
        super(requiredKey);
    }
    
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) { }
}
