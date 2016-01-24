package com.sproutlife.panel.gamepanel.handler;

import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.Vector;

import com.sproutlife.panel.PanelController;
import com.sproutlife.panel.gamepanel.handler.HandlerConsts.RequiredKey;

public class InteractionHandler implements MouseListener, MouseMotionListener,
                                                     MouseWheelListener {
    private enum MouseButton {
        Left, Right;
    }

    private PanelController gc;
    private MouseOverHandler mouseOverHandler;
    private boolean enableMouseOverOnDrag;
    private HandlerSet handlerSet;
    private RequiredKey dragRequiredKey;
    private RequiredKey clickRequiredKey;
    private Point mousePoint;
    private MouseButton mouseButtonPressed = null;
    
    public InteractionHandler(PanelController panelController) {
        this.gc = panelController;
        mouseOverHandler = new MouseOverHandler(panelController);
        dragRequiredKey = RequiredKey.None;
    }
    
    public HandlerSet getHandlerSet() { return handlerSet; }
    
    public void setHandlerSet(HandlerSet handlerSet) { 
        this.handlerSet = handlerSet; 
    }   
    
    private <T extends Handler> Vector<T> getHandlers(Iterable<T> handlers, RequiredKey requiredKey) {
        Vector<T> handlersToReturn = new Vector<T>();
        for ( T handler : handlers ) {
            if ( (handler.getRequiredKey() == requiredKey ||
                  handler.getRequiredKey() == RequiredKey.Any) ) {
                handlersToReturn.add(handler);
            }
        }
        return handlersToReturn;
    }
    
    
    public void updateMouseOver() {
        try {
            gc.getInteractionLock().writeLock().lock();
            //if ( !handlerSet.popupVisible() ) {
                mouseOverHandler.updateMouseOver(gc.getScrollController().getDrawPositionFromMousePosition(mousePoint));
            //}
        }
        finally {
            gc.getInteractionLock().writeLock().unlock();
        }
    }

    private static int getAllKeyModifiers() {
        return InputEvent.ALT_MASK + InputEvent.CTRL_MASK + InputEvent.SHIFT_MASK;
    }
    
    public static RequiredKey getRequiredKey(MouseEvent mouseEvent) {
        int modifiers = mouseEvent.getModifiers() & getAllKeyModifiers();
        if ( modifiers == 0 ) {
            return RequiredKey.None;
        }
        else if ( modifiers == InputEvent.ALT_MASK ) {
            return RequiredKey.Alt;
        }
        else if ( modifiers == InputEvent.CTRL_MASK ) {
            return RequiredKey.Control;
        }
        else if ( modifiers == InputEvent.SHIFT_MASK ) {
            return RequiredKey.Shift;
        }
        return null;
    }
    
    
    public void mousePressed(MouseEvent mouseEvent) {
        if ( mouseButtonPressed != null ) {
            return;
        }
        mouseButtonPressed = getMouseButton(mouseEvent);
        if ( mouseButtonPressed == MouseButton.Right ) {

        }
        if ( mouseButtonPressed == MouseButton.Left ) {
            
            for ( BackgroundDragHandler backgroundDragHandler : getHandlers(handlerSet.getBackgroundDragHandlers(), dragRequiredKey) ) {
                backgroundDragHandler.mousePressed(mouseEvent);
            }
            
            for ( DragHandler dragHandler : getHandlers(handlerSet.getDragHandlers(), dragRequiredKey) ) {
                dragHandler.mousePressed(mouseEvent);
            }
        }
    }
    
    public void mouseReleased(MouseEvent mouseEvent) {        
        if ( mouseButtonPressed == MouseButton.Right &&
             getMouseButton(mouseEvent) == MouseButton.Right ) {
            Point2D.Double mouseLocation = gc.getScrollController().getDrawPositionFromMousePosition(mousePoint);

            mouseButtonPressed = null;
        }
        if ( mouseButtonPressed == MouseButton.Left &&
             getMouseButton(mouseEvent) == MouseButton.Left ) {
            
                boolean mouseOverChanged = false;
                try {
                    gc.getInteractionLock().readLock().lock();
                    mouseOverChanged = false; //update value
                }
                finally {
                    gc.getInteractionLock().readLock().unlock();
                }
                for ( BackgroundDragHandler backgroundDragHandler : getHandlers(handlerSet.getBackgroundDragHandlers(), dragRequiredKey) ) {
                    backgroundDragHandler.mouseReleased(mouseEvent, mouseOverChanged);
                }
            
            for ( DragHandler dragHandler : getHandlers(handlerSet.getDragHandlers(), dragRequiredKey) ) {
                dragHandler.mouseReleased(mouseEvent);
            }
            mouseButtonPressed = null;
        }
    }
    
    public void setEnableMouseOverOnDrag(boolean enableMouseOverOnDrag) {
        //FIXME - should drag handlers that need this just call updateMouseOver?
        this.enableMouseOverOnDrag = enableMouseOverOnDrag;
    }
    
    public void mouseDragged(MouseEvent mouseEvent) {
        if ( mouseButtonPressed == MouseButton.Left ) {
            updateMousePosition(mouseEvent, enableMouseOverOnDrag);

            boolean mouseOverChanged = false;
            try {
                gc.getInteractionLock().readLock().lock();
                mouseOverChanged = false; //update value
            }
            finally {
                gc.getInteractionLock().readLock().unlock();
            }
            for ( BackgroundDragHandler backgroundDragHandler : getHandlers(handlerSet.getBackgroundDragHandlers(), dragRequiredKey) ) {
                backgroundDragHandler.mouseDragged(mouseEvent, mouseOverChanged);
            }

            for ( DragHandler dragHandler : getHandlers(handlerSet.getDragHandlers(), dragRequiredKey) ) {
                dragHandler.mouseDragged(mouseEvent);
            }
        }
    }
    
    public void mouseClicked(MouseEvent mouseEvent) {

        if ( getMouseButton(mouseEvent) == MouseButton.Left ) {
            if ( mouseEvent.getClickCount() == 1 ) {
                
                clickRequiredKey = getRequiredKey(mouseEvent);
                
                for ( BackgroundClickHandler backgroundClickHandler : getHandlers(handlerSet.getBackgroundClickHandlers(), clickRequiredKey) ) {
                    backgroundClickHandler.mouseClicked(mouseEvent);
                }

                for ( ClickHandler clickHandler : getHandlers(handlerSet.getClickHandlers(), clickRequiredKey) ) {
                    clickHandler.mouseClicked(mouseEvent);
                }
            }
            if ( mouseEvent.getClickCount() == 2 ) {

                boolean mouseOverChanged = false;                

                for (BackgroundClickHandler backgroundClickHandler : getHandlers(
                        handlerSet.getBackgroundClickHandlers(),
                        clickRequiredKey)) {
                    backgroundClickHandler.mouseDoubleClicked(mouseEvent,
                            mouseOverChanged);
                }

                for (ClickHandler clickHandler : getHandlers(
                        handlerSet.getClickHandlers(), clickRequiredKey)) {
                    clickHandler.mouseDoubleClicked(mouseEvent);
                }
            }
        }
    }
    
    public void mouseMoved(MouseEvent mouseEvent) {
        updateMousePosition(mouseEvent);
    }
    
    public void mouseEntered(MouseEvent mouseEvent) {
        updateMousePosition(mouseEvent);
    }
    
    private void updateMousePosition(MouseEvent mouseEvent) {
        updateMousePosition(mouseEvent, true);
    }
    
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        for ( MouseWheelHandler mouseWheelHandler : getHandlers(handlerSet.getMouseWheelHandlers(), getRequiredKey(mouseWheelEvent)) ) {
            mouseWheelHandler.mouseWheelMoved(mouseWheelEvent);
        }
    }
    
    private void updateMousePosition(MouseEvent mouseEvent, boolean updateMouseOver) {
        try {
            gc.getInteractionLock().writeLock().lock();
            mousePoint = mouseEvent.getPoint();
            if ( updateMouseOver ) {
                mouseOverHandler.mouseMoved(gc.getScrollController().getDrawPositionFromMousePosition(mousePoint));
            }
        }
        finally {
            gc.getInteractionLock().writeLock().unlock();
        }
    }
    
    public void mouseExited(MouseEvent mouseEvent) {

        mousePoint = null;

        mouseOverHandler.mouseExited();


    }
    
    private static MouseButton getMouseButton(MouseEvent me) {
        if ( (me.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK ) {
            return MouseButton.Left;
        }
        if ( (me.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK ) {
            return MouseButton.Right;
        }
        return null;
    }
    
    private static boolean isMouseButtonDown(MouseEvent me, MouseButton mouseButton) {
        boolean leftButtonDown =
            (me.getModifiers() & InputEvent.BUTTON1_DOWN_MASK) == InputEvent.BUTTON1_DOWN_MASK;
        boolean rightButtonDown =
            (me.getModifiers() & InputEvent.BUTTON3_DOWN_MASK) == InputEvent.BUTTON3_DOWN_MASK;
        if ( mouseButton == MouseButton.Left ) {
            return leftButtonDown;
        }
        if ( mouseButton == MouseButton.Right ) {
            return rightButtonDown;
        }
        return !leftButtonDown && !rightButtonDown; // Can check for neither left or right 
    }
}