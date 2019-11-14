package com.sproutlife.renderer.colors;

import java.awt.Color;

import com.sproutlife.model.echosystem.Organism;

public class AngleColorModel extends ColorModel {
    private float primaryHue = 0f;
    private float hueRange = 100;

    @Override
    public void setAttribute(String attribute, Object value) {
        if (attribute.equals("primaryHue")) {
            primaryHue = ((int) value)/360f;
        }
        if (attribute.equals("hueRange")) {
            hueRange = (int) value;
        }
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
        if (getBackgroundTheme() == BackgroundTheme.white) {
            return getColor(o, primaryHue, 0.7f, 1f);
        }
        else {
            return getColor(o, primaryHue, 0.6f, 1f);
        }
    }

    @Override
    public Color getHeadColor(Organism o) {
        if (getBackgroundTheme() == BackgroundTheme.white) {
            return getColor(o, primaryHue, 1f, 0.8f);
        }
        else {
            return getColor(o, primaryHue, 0.8f, 1f);
        }
    }

    @Override
    public Color getTailColor(Organism o) {
        return getHeadColor(o);
    }

    @Override
    public Color getGenomeBackgroundColor(Organism o) {
        return getColor(o, primaryHue, 0.2f, 1f);
    }

    private Color getColor(Organism o, float h, float s, float br) {
        Organism[] ancestors = new Organism[4];
        Organism a = o;
        for (int ai=0;ai<ancestors.length;ai++) {
            ancestors[ai]=a;
            a = a.getParent();
            if (a==null) {
                return getRandomColor(o);
            }
        }
        double dp1 = getDotProduct(ancestors[0], ancestors[1], ancestors[2]);
        double dp2 = getDotProduct(ancestors[1], ancestors[2], ancestors[3]);

        float r1 = getRotation(dp1);
        float r2 = getRotation(dp2);

        Color c1 = new Color(Color.HSBtoRGB(h+r1, s, br));
        Color c2 = new Color(Color.HSBtoRGB(h+r2, s, br));

        int r = (c1.getRed()*3+c2.getRed()*2)/5;
        int g = (c1.getGreen()*3+c2.getGreen()*2)/5;
        int b = (c1.getBlue()*3+c2.getBlue()*2)/5;

        return new Color(r,g,b);
    }

    private double getDotProduct(Organism o, Organism p, Organism g) {
        double dx1 = o.x-p.x;
        double dy1 = o.y-p.y;
        double dx2 = p.x-g.x;
        double dy2 = p.y-g.y;
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
        //normalized DP should never be more than 1, but sometimes floating point errors occur.
        if (dp>1 || dp<-1) {
           return 1;
        }
        return dp;
    }

    private float getRotation(double dp) {
        float ro = (float) (Math.acos(dp)/Math.PI);
        if (ro>0.5) {
            ro = ro-1;
        }
        return ro*(hueRange/100f);
    }

    private Color getRandomColor(Organism o) {
        float randomHash = (float) ((o.x+o.y)%100/100.0);
        return new Color(Color.HSBtoRGB(randomHash, 0.5f, 1f));
    }
}
