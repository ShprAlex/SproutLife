package com.sproutlife.panel.gamepanel;

import java.awt.*;
import java.awt.geom.*;
import java.util.concurrent.locks.ReentrantLock;

import com.sproutlife.panel.PanelController;
import com.sproutlife.panel.gamepanel.ScrollPanel.ViewportMovedListener;
import com.sproutlife.panel.gamepanel.ScrollPanel.ViewportResizedListener;

public class ScrollPanelController {
    
    private static final int SUBTRACT_FROM_MARGIN = 100;
    private static final int SCROLL_TO_DELAY = 18;
    private static final int SCROLL_TO_STEP = 8; 
    
    private PanelController gc;
    private Rectangle visibleRectangle;
    private Rectangle maxVisibleRectangle;
    private Rectangle2D.Double rendererBounds;
    private Point2D.Double centerLocation;
    private Thread scrollThread;
    private boolean autoScrolling;
    private ReentrantLock autoScrollLock;
    
    ScrollPanel scrollPanel;
    
    public ScrollPanelController(PanelController panelController) {
        this.gc = panelController;
        visibleRectangle = new Rectangle(0, 0, 1, 1);
        maxVisibleRectangle = new Rectangle();
        autoScrollLock = new ReentrantLock();
        
        scrollPanel = new ScrollPanel(panelController);
        
        createListeners();
    }
    
    public ScrollPanel getScrollPanel() {
        return scrollPanel;
    }
    
    private void createListeners() {
        ViewportResizedListener viewportResizedListener = new ViewportResizedListener() {
            public void viewportResized(int viewportWidth, int viewportHeight) {
                ScrollPanelController.this.viewportResized(viewportWidth, viewportHeight);
            }
        };
        ViewportMovedListener viewportMovedListener = new ViewportMovedListener() {
            public void viewportMoved(int viewportX, int viewportY) {
                stopAutoScroll();
                ScrollPanelController.this.viewportMoved(viewportX, viewportY);
            }
        };
        getScrollPanel().addViewportResizedListener(viewportResizedListener);
        getScrollPanel().addViewportMovedListener(viewportMovedListener);
    }
    
    public boolean isAutoScrolling() { return autoScrolling; }
    
    public synchronized void setScalingZoomFactor(double scalingZoomFactor) {
        //if ( getScalingZoomFactor() != scalingZoomFactor ) { // FIXME - this was always false for skeleton based i think
            try {
                gc.getInteractionLock().writeLock().lock();
                double viewportX = getViewportOffsetX();
                double viewportY = getViewportOffsetY();
                gc.getBoardRenderer().getTransform().setToScale(scalingZoomFactor,scalingZoomFactor);
                gc.getBoardRenderer().getTransform().translate(viewportX, viewportY);
                updateScrollBarsWithCenter(getCenterDrawPosition());
            }
            finally {
                gc.getInteractionLock().writeLock().unlock();
            }
            gc.getImageManager().repaintNewImage();
        //}
    }
    
    public double getScalingZoomFactor() { return gc.getBoardRenderer().getZoom(); }
    
    public Rectangle getMaxVisibleRectangle() {
        return maxVisibleRectangle;
    }
    
    public Rectangle2D.Double getRendererRectangle() {
        return getRendererRectangle(0);
    }
    
    public Rectangle2D.Double getRendererRectangle(int pad) {
        double x = (rendererBounds.getMinX()*getScalingZoomFactor()) - pad;
        double y = (rendererBounds.getMinY()*getScalingZoomFactor()) - pad;
        double width = (rendererBounds.getMaxX()*getScalingZoomFactor()) - x + pad;
        double height = (rendererBounds.getMaxY()*getScalingZoomFactor()) - y + pad;
        return new Rectangle2D.Double(x, y, width, height);
    }
    
    private static Rectangle2D.Double applyTransform(Rectangle2D.Double rect, AffineTransform transform) {
        Rectangle2D.Double newRect = new Rectangle2D.Double();
        Rectangle2D transRect = transform.createTransformedShape(rect).getBounds2D();
        newRect.setRect(transRect);
        return newRect;
    }
    
    public double getViewportOffsetX() {
        return gc.getBoardRenderer().getTransform().getTranslateX() /
               gc.getBoardRenderer().getTransform().getScaleX();
    }

    public double getViewportOffsetY() {
        return gc.getBoardRenderer().getTransform().getTranslateY() /
               gc.getBoardRenderer().getTransform().getScaleY();
    }
    
    public Rectangle2D.Double getViewportRendererBounds() {
        Rectangle2D.Double viewportBounds = null;
        try {
            gc.getInteractionLock().readLock().lock();
            viewportBounds = new Rectangle2D.Double(-getViewportOffsetX(),
                                                    -getViewportOffsetY(),
                                                    visibleRectangle.width/getScalingZoomFactor(),
                                                    visibleRectangle.height/getScalingZoomFactor());
        }
        finally {
            gc.getInteractionLock().readLock().unlock();
        }
        return viewportBounds;
    }
    
    public Rectangle getViewportRendererBounds(int scaledWidth, int scaledHeight) {
        Rectangle viewportBounds = null;
        try {
            gc.getInteractionLock().readLock().lock();
            int x = (int)(((-getViewportOffsetX() - rendererBounds.x) / rendererBounds.width) * scaledWidth);
            int y = (int)(((-getViewportOffsetY() - rendererBounds.y) / rendererBounds.height) * scaledHeight);
            int width = (int)(((visibleRectangle.width/getScalingZoomFactor()) / rendererBounds.width) * scaledWidth);
            int height = (int)(((visibleRectangle.height/getScalingZoomFactor()) / rendererBounds.height) * scaledHeight);
            viewportBounds = new Rectangle(x, y, width, height);
        }
        finally {
            gc.getInteractionLock().readLock().unlock();
        }
        return viewportBounds;
    }
    
    public boolean isDrawLocationOnScreen(Point2D.Double drawLocation) {
        drawLocation.x *= getScalingZoomFactor();
        drawLocation.y *= getScalingZoomFactor();
        return visibleRectangle.contains(drawLocation);
    }
    
    public Point getScreenLocationFromDrawLocation(Point2D.Double drawLocation) {
        Point screenLocation = null;
        try {
            gc.getInteractionLock().readLock().lock();
            screenLocation = new Point((int)((drawLocation.x + getViewportOffsetX())*getScalingZoomFactor()),
                                       (int)((drawLocation.y + getViewportOffsetY())*getScalingZoomFactor()));
        }
        finally {
            gc.getInteractionLock().readLock().unlock();
        }
        return screenLocation;
    }
    
    public Rectangle2D.Double getDrawRectangleFromMouseRectangle(Rectangle mouseRectangle) {
        Rectangle2D.Double drawRectangle = null;
        try {
            gc.getInteractionLock().readLock().lock();
            drawRectangle = new Rectangle2D.Double(mouseRectangle.getMinX()/getScalingZoomFactor()-getViewportOffsetX(),
                                                   mouseRectangle.getMinY()/getScalingZoomFactor()-getViewportOffsetY(),
                                                   mouseRectangle.getWidth()/getScalingZoomFactor(), mouseRectangle.getHeight()/getScalingZoomFactor());
        }
        finally {
            gc.getInteractionLock().readLock().unlock();
        }
        return drawRectangle;
    }
    
    public Point2D.Double getDrawPositionFromMousePosition(Point mousePosition) {
        if ( mousePosition == null ) {
            return null;
        }
        Point2D.Double drawPosition = null;
        try {
            gc.getInteractionLock().readLock().lock();
            drawPosition = new Point2D.Double(mousePosition.x/getScalingZoomFactor()-getViewportOffsetX(),
                                              mousePosition.y/getScalingZoomFactor()-getViewportOffsetY());
        }
        finally {
            gc.getInteractionLock().readLock().unlock();
        }
        return drawPosition;
    }
    
    public Rectangle getVisibleRectangle() {
        try {
            gc.getInteractionLock().readLock().lock();
            return visibleRectangle;
        }
        finally {
            gc.getInteractionLock().readLock().unlock();
        }
    }
    
    public Point2D.Double getCenterDrawPosition() {
        try {
            gc.getInteractionLock().readLock().lock();
            return centerLocation;
        }
        finally {
            gc.getInteractionLock().readLock().unlock();
        }
    }
    
    public Point getCenterDrawPoint() {
        Point centerDrawPoint = null;
        try {
            gc.getInteractionLock().readLock().lock();
            centerDrawPoint = new Point((int)(visibleRectangle.width/(2*getScalingZoomFactor())-getViewportOffsetX()),
                                        (int)(visibleRectangle.height/(2*getScalingZoomFactor())-getViewportOffsetY()));
        }
        finally {
            gc.getInteractionLock().readLock().unlock();
        }
        return centerDrawPoint;
    }
    
    public Point2D.Double shiftFromCenterToTopLeft(Point2D.Double centerLocation) {
        Point2D.Double topLeftLocation = new Point2D.Double();
        topLeftLocation.x = centerLocation.x - visibleRectangle.width/2.0;
        topLeftLocation.y = centerLocation.y - visibleRectangle.height/2.0;
        return topLeftLocation;
    }
    
    public Point shiftFromCenterToTopLeft(Point centerLocation) {
        Point topLeftLocation = new Point();
        topLeftLocation.x = centerLocation.x - visibleRectangle.width/2;
        topLeftLocation.y = centerLocation.y - visibleRectangle.height/2;
        return topLeftLocation;
    }
    
    private void viewportMoved(int viewportX, int viewportY) {
        if ( gc.getBoardRenderer() != null ) {
            try {
                gc.getInteractionLock().readLock().lock();
                updateViewportOffset(viewportX, viewportY);
            }
            finally {
                gc.getInteractionLock().readLock().unlock();
            }
            gc.getImageManager().repaintNewImage();
        }
    }
    
    private Point2D.Double calculateCenterDrawPosition() {
        return new Point2D.Double(visibleRectangle.width/(2*getScalingZoomFactor())-getViewportOffsetX(),
                                  visibleRectangle.height/(2*getScalingZoomFactor())-getViewportOffsetY());
    }
    
    public void viewportResized(int viewportWidth, int viewportHeight) {
        if ( viewportWidth > 0 && viewportHeight > 0 ) {
            try {
                gc.getInteractionLock().writeLock().lock();
                visibleRectangle.width = viewportWidth;
                visibleRectangle.height = viewportHeight;
                gc.getImageManager().createImage();
                updateScrollBarsWithCenter(centerLocation);
                gc.getImageManager().repaintNewImage();
            }
            finally {
                gc.getInteractionLock().writeLock().unlock();
            }
        }
    }
    
    private void updateViewportOffset(int x, int y) {
        double xoffset = x / getScalingZoomFactor();
        double yoffset = y / getScalingZoomFactor();
        gc.getBoardRenderer().getTransform().setToScale(getScalingZoomFactor(),getScalingZoomFactor());
        gc.getBoardRenderer().getTransform().translate(-xoffset, -yoffset);
        
        visibleRectangle.x = x;
        visibleRectangle.y = y;
        centerLocation = calculateCenterDrawPosition();
        gc.getBoardRenderer().updateVisibleRenderers(getViewportRendererBounds());
    }
    
    private void updateViewportOffset(int x, int y, int hExtent, int vExtent,
                                      int minX, int maxX, int minY, int maxY) {
        double xoffset = x / getScalingZoomFactor();
        double yoffset = y / getScalingZoomFactor();
        gc.getBoardRenderer().getTransform().setToScale(getScalingZoomFactor(),getScalingZoomFactor());
        gc.getBoardRenderer().getTransform().translate(-xoffset, -yoffset);
        visibleRectangle.x = x;
        visibleRectangle.y = y;
        visibleRectangle.width = hExtent;
        visibleRectangle.height = vExtent;
        maxVisibleRectangle.x = minX;
        maxVisibleRectangle.y = minY;
        maxVisibleRectangle.width = maxX - minX;
        maxVisibleRectangle.height = maxY - minY;
        centerLocation = calculateCenterDrawPosition();
        
        gc.getBoardRenderer().updateVisibleRenderers(getViewportRendererBounds());
    }
    
    public void updateScrollBars() {
        updateScrollBarsWithCenter(null);
    }

    public void updateScrollBarsWithCenter() {
        updateScrollBarsWithCenter(getCenterDrawPosition());
    }
            
    public void updateScrollBarsWithCenter(Point2D.Double centerPosition) {
        try {
            gc.getInteractionLock().writeLock().lock();
            rendererBounds = null;
            rendererBounds = gc.getBoardRenderer().getRendererBounds();
            if (rendererBounds == null) {
                gc.updateBoardSizeFromPanelSize(getScrollPanel().getViewportSize()); 
                rendererBounds = gc.getBoardRenderer().getRendererBounds();
            }
            
            int xExtent = visibleRectangle.width;
            int yExtent = visibleRectangle.height;
            /*
            int minX = (int)(rendererBounds.getMinX()*getScalingZoomFactor()) - xExtent + SUBTRACT_FROM_MARGIN;
            int maxX = (int)(rendererBounds.getMaxX()*getScalingZoomFactor()) + xExtent - SUBTRACT_FROM_MARGIN;
            int minY = (int)(rendererBounds.getMinY()*getScalingZoomFactor()) - yExtent + SUBTRACT_FROM_MARGIN;
            int maxY = (int)(rendererBounds.getMaxY()*getScalingZoomFactor()) + yExtent - SUBTRACT_FROM_MARGIN;
            */
            
            int minX = (int)(rendererBounds.getMinX()*getScalingZoomFactor());// -  SUBTRACT_FROM_MARGIN;
            int maxX = (int)(rendererBounds.getMaxX()*getScalingZoomFactor());// +  SUBTRACT_FROM_MARGIN;
            int minY = (int)(rendererBounds.getMinY()*getScalingZoomFactor());// -  SUBTRACT_FROM_MARGIN;
            int maxY = (int)(rendererBounds.getMaxY()*getScalingZoomFactor());// +  SUBTRACT_FROM_MARGIN;
            
            if (maxX-minX<xExtent) {
                int xDiff = xExtent-(maxX-minX);
                minX-=xDiff/2;
                maxX+=xDiff/2;                
            }
            if (maxY-minY<yExtent) {
                int yDiff = yExtent-(maxY-minY);
                minY-=yDiff/2;
                maxY+=yDiff/2;                
            }
            
            int xValue = validate(visibleRectangle.x, minX, maxX, xExtent);
            int yValue = validate(visibleRectangle.y, minY, maxY, yExtent);
            
            if ( centerPosition != null ) {
                Point2D.Double zoomCenterPosition = new Point2D.Double();
                zoomCenterPosition.x = centerPosition.x * getScalingZoomFactor();
                zoomCenterPosition.y = centerPosition.y * getScalingZoomFactor();
                Point2D.Double topLeftPosition = shiftFromCenterToTopLeft(zoomCenterPosition);
                xValue = validate((int)Math.round(topLeftPosition.x), minX, maxX, xExtent);
                yValue = validate((int)Math.round(topLeftPosition.y), minY, maxY, yExtent);
            }
            
            getScrollPanel().update(minX, maxX, xValue, xExtent, minY, maxY, yValue, yExtent);
            
            updateViewportOffset(xValue, yValue, xExtent, yExtent,
                                 minX, maxX, minY, maxY);
            if ( !(rendererBounds.getMinX() == rendererBounds.getMaxX() &&
                   rendererBounds.getMinY() == rendererBounds.getMaxY()) &&
                centerPosition != null ) {
                centerLocation = centerPosition;
            }
        }
        finally {
            gc.getInteractionLock().writeLock().unlock();
        }
    }
    
    private int validate(int val, int min, int max, int extent) {
        if ( val < min ) {
            return min;
        }
        else if ( val > max - extent ) {
            return max - extent;
        }
        else {
            return val;
        }
    }
    
    public void scrollBy(int deltaX, int deltaY) {
        scrollBy(deltaX, deltaY, true);
    }
    
    private void scrollBy(int deltaX, int deltaY, boolean cancelAutoScroll) {
        if ( cancelAutoScroll ) {
            stopAutoScroll();
        }
        if ( deltaX != 0 || deltaY != 0 ) {
            scrollTo(visibleRectangle.x + deltaX,
                     visibleRectangle.y + deltaY);
        }
    }
    
    public void scrollTo(int x, int y) {
        try {
            gc.getInteractionLock().readLock().lock();
            final int validX = validate(x, maxVisibleRectangle.x,
                                           maxVisibleRectangle.x + maxVisibleRectangle.width,
                                           visibleRectangle.width);
            final int validY = validate(y, maxVisibleRectangle.y,
                                           maxVisibleRectangle.y + maxVisibleRectangle.height,
                                           visibleRectangle.height);
            
            getScrollPanel().update(validX, validY);
            
            updateViewportOffset(validX, validY);
            centerLocation = calculateCenterDrawPosition();
        }
        finally {
            gc.getInteractionLock().readLock().unlock();
        }
        gc.getImageManager().repaintNewImage();
    }
    
    private void scrollToCenter(int x, int y) {
        scrollTo(x-visibleRectangle.width/2, y-visibleRectangle.height/2);
    }
    
    public void scrollToCenterFromPercentBounds(double xPercent, double yPercent) {
        int xPad = visibleRectangle.width - SUBTRACT_FROM_MARGIN;
        int yPad = visibleRectangle.height - SUBTRACT_FROM_MARGIN;
        int x = (int)(xPad + maxVisibleRectangle.x + xPercent * (maxVisibleRectangle.width - 2*xPad));
        int y = (int)(yPad + maxVisibleRectangle.y + yPercent * (maxVisibleRectangle.height - 2*yPad));
        scrollToCenter(x, y);
    }
    
    private void scrollDrawLocationToCenter(Point2D.Double drawPoint) {
        scrollToCenter((int)(drawPoint.x * getScalingZoomFactor()), (int)(drawPoint.y * getScalingZoomFactor()));
    }
    
    private int ceilFromZero(double value) {
        if ( value > 0 ) {
            return (int)Math.ceil(value);
        }
        else {
            return (int)Math.floor(value);
        }
    }

    private void scale(Point2D.Double point, double scale) {
        point.x*= scale;
        point.y*= scale;
    }
    
    public void slowScrollToCenter(Point2D.Double scrollPoint) {
        stopAutoScroll();
        if (scrollPoint!=null) {
            scrollThread = new AutoScrollThread(scrollPoint);
            scrollThread.start();
        }        
    }

    class AutoScrollThread extends Thread {
        private Point2D.Double scrollPoint;
        
        public AutoScrollThread(Point2D.Double scrollPoint) {
            this.scrollPoint = scrollPoint;
        }
        
        public void run() {
            try {
                autoScrollLock.lock();
                autoScrolling = true;
                Point2D.Double centerLocation = new Point2D.Double();
                centerLocation.setLocation(getCenterDrawPosition());
                scale(centerLocation, getScalingZoomFactor());
                Point2D.Double visibleScrollPoint = null;
                try {
                    gc.getInteractionLock().readLock().lock();
                    visibleScrollPoint = new Point2D.Double(scrollPoint.x,scrollPoint.y);//.getDrawLocation();
                    scale(visibleScrollPoint, getScalingZoomFactor());
                }
                finally {
                    gc.getInteractionLock().readLock().unlock();
                }
                
                double distance = visibleScrollPoint.distance(centerLocation);
                while ( autoScrolling && distance > SCROLL_TO_STEP ) {
                    double distancePercent = 1.0 - (distance - SCROLL_TO_STEP) / distance;
                    double deltaX = visibleScrollPoint.x - centerLocation.x;
                    double deltaY = visibleScrollPoint.y - centerLocation.y;
                    int moveX = ceilFromZero(deltaX * distancePercent);
                    int moveY = ceilFromZero(deltaY * distancePercent);
                    scrollBy(moveX, moveY, false);
                    try {
                        Thread.sleep(SCROLL_TO_DELAY);
                    }
                    catch (InterruptedException ex) {
                        break;
                    }
                    centerLocation.setLocation(getCenterDrawPosition());
                    scale(centerLocation, getScalingZoomFactor());
                    try {
                        gc.getInteractionLock().readLock().lock();
                        visibleScrollPoint = new Point2D.Double(scrollPoint.x,scrollPoint.y);
                        scale(visibleScrollPoint, getScalingZoomFactor());
                    }
                    finally {
                        gc.getInteractionLock().readLock().unlock();
                    }
                    distance = visibleScrollPoint.distance(centerLocation);
                    //gc.getInteractionHandler().updateMouseOver();
                }
                if ( autoScrolling ) {
                    try {
                        gc.getInteractionLock().readLock().lock();
                        scrollDrawLocationToCenter(new Point2D.Double(scrollPoint.x,scrollPoint.y));
                    }
                    finally {
                        gc.getInteractionLock().readLock().unlock();
                    }
                }
                //gc.getInteractionHandler().updateMouseOver();
                autoScrolling = false;
            }
            finally {
                autoScrollLock.unlock();
            }
        }
    }
    
    public void stopAutoScroll() {
        if ( scrollThread != null && scrollThread.isAlive() ) {
            autoScrolling = false;
            try {
                scrollThread.interrupt();
            }
            catch (Exception ex) {
                //FIXME: Applets don't allow threads to be interrupted
            }
            //gc.getInteractionHandler().updateMouseOver();
        }
    }
}