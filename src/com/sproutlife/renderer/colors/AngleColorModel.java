package com.sproutlife.renderer.colors;

import java.awt.Color;

import com.sproutlife.model.echosystem.Organism;

public class AngleColorModel extends ColorModel {
    private float primaryHue = 0f;
    private float hueRangeMultiplier = 200;

    @Override
    public void setAttribute(String attribute, Object value) {
        if (attribute.equals("primaryHue")) {
            primaryHue = ((int) value%6)/6f;
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
        if(o.getParent()==null || o.getParent().getParent()==null) {
            float rand = (float) ((o.x+o.y)%100/100.0);
            return new Color(Color.HSBtoRGB(rand, 0.5f, 1f));
        }

        float ro = getRotation(o);
        float rp = getRotation(o.getParent());

        Color c1 = new Color(Color.HSBtoRGB(h+ro, s, br));
        Color c2 = new Color(Color.HSBtoRGB(h+rp, s, br));

        int r = (c1.getRed()*3+c2.getRed()*2)/5;
        int g = (c1.getGreen()*3+c2.getGreen()*2)/5;
        int b = (c1.getBlue()*3+c2.getBlue()*2)/5;

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
        //normalized DP should never be more than 1, but sometimes floating point errors occur.
        if (dp>1 || dp<-1) {
           return 0;
        }
        float ro = (float) (Math.acos(dp)/Math.PI/2);
        if (ro>0.5) {
            ro = -1+ro;
        }
        return ro*(hueRangeMultiplier/100f);
    }
}
