package com.sproutlife.panel.gamepanel.handler;

import java.awt.geom.Point2D;

import com.sproutlife.panel.PanelController;

public class MouseOverHandler extends Handler {
    private PanelController gc;
    
    public MouseOverHandler(PanelController panelController) {
        super(null);
        this.gc = panelController;
    }
    
    public void mouseMoved(Point2D.Double mousePosition) {
        updateMouseOver(mousePosition);

        gc.getImageManager().repaintNewImage();

 
    }
    
    public void mouseExited() {

        gc.getImageManager().repaintNewImage();

    }
    
    protected void updateMouseOver(Point2D.Double mousePosition) {
        try {
            gc.getInteractionLock().writeLock().lock();
        }
        finally {
            gc.getInteractionLock().writeLock().unlock();
        }
    }
}
