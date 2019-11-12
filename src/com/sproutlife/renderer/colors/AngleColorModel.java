package com.sproutlife.renderer.colors;

import java.awt.Color;

import com.sproutlife.model.echosystem.Organism;

public class AngleColorModel implements ColorModel {
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
        Color c = null;
        if (getBackgroundTheme() == BackgroundTheme.white) {
            int grayC = 140;
            c =  new Color(grayC-80, 255, grayC-80);
        }
        else {
            int grayC = 100;
            c =  new Color(grayC-10, 255, grayC-10);
        }
        return hsvRotate(c, o);
    }

    @Override
    public Color getHeadColor(Organism o) {
        Color c = new Color(0, 230, 0);
        return hsvRotate(c, o);
    }

    @Override
    public Color getTailColor(Organism o) {
        return getHeadColor(o);
    }

    @Override
    public Color getGenomeBackgroundColor(Organism o) {
        int grayC = 210;
        Color c = new Color(grayC, 255, grayC);
        return hsvRotate(c, o);
    }

    private Color hsvRotate(Color c, Organism o) {
        if(o.getParent()==null) {
            float rand = (float) ((o.x+o.y)%100/100.0);
            return new Color(Color.HSBtoRGB(rand, 0.5f, 1f));
        }
        float ro = getRotation(o);
        float rp = getRotation(o.getParent());

        float[] hsbVals = new float[3];
        Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsbVals);
        Color c1 = new Color(Color.HSBtoRGB(hsbVals[0]+ro, hsbVals[1], hsbVals[2]));
        Color c2 = new Color(Color.HSBtoRGB(hsbVals[0]+rp, hsbVals[1], hsbVals[2]));

        int r = (c1.getRed()+c2.getRed())/2;
        int g = (c1.getGreen()+c2.getGreen())/2;
        int b = (c1.getBlue()+c2.getBlue())/2;

        return new Color(r,g,b);
    }

    private float getRotation(Organism o) {
        Organism p = o.getParent();
        if (p==null) {
            return 0;
        }
        Organism g = p.getParent();
        if (g == null) {
            return 0;
        }
        float dx1 = o.x-p.x;
        float dy1 = o.y-p.y;
        float dx2 = p.x-g.x;
        float dy2 = p.y-g.y;
        double dist1 = Math.sqrt(dx1*dx1+dy1*dy1);
        double dist2 = Math.sqrt(dx2*dx2+dy2*dy2);
        if (dist1>0) {
            dx1/=dist1;
            dy1/=dist1;
        }
        if(dist2>0) {
            dx2/=dist2;
            dy2/=dist2;
        }
        double dp = dx1*dx2+dy1*dy2;
        float ro = (float) (Math.acos(dp)/Math.PI);
        if (ro>0.5) {
            ro = -1+ro;
        }
        return ro;
    }
}
