package com.sproutlife.renderer.colors;

import java.awt.Color;

import com.sproutlife.model.echosystem.Organism;

public class DefaultColorModel implements ColorModel {
    BackgroundTheme backgroundTheme = BackgroundTheme.black;

    @Override
    public BackgroundTheme getBackgroundTheme() {
        return backgroundTheme;
    }

    @Override
    public void setBackgroundTheme(BackgroundTheme t) {
        this.backgroundTheme = t;
    }

    @Override
    public Color getBackgroundColor() {
        switch (backgroundTheme) {
            case black:
                return Color.black;
            default:
                return Color.white;
        }
    }

    @Override
    public Color getCellColor(Organism o) {
        int grayC = 100;
        switch (o.getAttributes().kind) {
            case 0: return new Color(255, grayC, grayC);
            case 1: return new Color(grayC-10, 255, grayC-10);
            case 2: return new Color(grayC, grayC ,255);
        }
        return null;
    }

    @Override
    public Color getHeadColor(Organism o) {
        int grayC = 186;
        switch (o.getAttributes().kind) {
            case 0: return new Color(255, grayC-10, grayC-10);
            case 1: return new Color(grayC, 255, grayC);
            case 2: return new Color(grayC, grayC, 255);
        }
        return null; 
    }

    @Override
    public Color getTailColor(Organism o) {
        return getHeadColor(o);
    }

    @Override
    public Color getGenomeBackgroundColor(Organism o) {
        int grayC = 210;
        switch (o.getAttributes().kind) {
            case 0: return new Color(255, grayC-10, grayC-10);
            case 1: return new Color(grayC, 255, grayC);
            case 2: return new Color(grayC, grayC,255);
        }
        return null;
    }
}
