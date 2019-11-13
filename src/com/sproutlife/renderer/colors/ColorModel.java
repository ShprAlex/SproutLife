/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.renderer.colors;

import java.awt.Color;

import com.sproutlife.model.echosystem.Organism;
import com.sproutlife.renderer.colors.ColorModel.BackgroundTheme;

public abstract class ColorModel {
    public static enum BackgroundTheme {
        black,
        white;
    }

    BackgroundTheme backgroundTheme = BackgroundTheme.black;

    public BackgroundTheme getBackgroundTheme() {
        return backgroundTheme;
    }

    public void setBackgroundTheme(BackgroundTheme t) {
        this.backgroundTheme = t;
    }

    public void setAttribute(String attribute, Object value) {};

    public abstract Color getBackgroundColor();

    public abstract Color getCellColor(Organism o);

    public abstract Color getHeadColor(Organism o);

    public abstract Color getTailColor(Organism o);

    public abstract Color getGenomeBackgroundColor(Organism o);
}
