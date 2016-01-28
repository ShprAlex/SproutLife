/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.panel.gamepanel.handler;

import com.sproutlife.panel.gamepanel.handler.HandlerConsts.RequiredKey;

public abstract class Handler {
    private RequiredKey requiredKey;
    
    public Handler(RequiredKey requiredKey) {
        this.requiredKey = requiredKey;
    }
    
    public RequiredKey getRequiredKey() { return requiredKey; }
    public void setRequiredKey(RequiredKey rk) { requiredKey = rk; }
}
