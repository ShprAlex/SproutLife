/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.panel.gamepanel;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sproutlife.panel.PanelController;

@SuppressWarnings("serial")
public class ScrollPanel extends JPanel {
    private static final int SCROLL_UNIT_INCREMENT = 10;
    private static final int SCROLL_BLOCK_INCREMENT_SUBTRACT_FROM_EXTENT = 10;
    private static final int MIN_SCROLL_BLOCK_INCREMENT = 10;
    
    private PanelController panelController;
    private Vector<ViewportResizedListener> viewportResizedListeners;
    private Vector<ViewportMovedListener> viewportMovedListeners;
    private BoundedRangeModel hScrollModel;
    private BoundedRangeModel vScrollModel;
    private JScrollBar hScrollBar;
    private JScrollBar vScrollBar;
    private JPanel innerPanel;
    private boolean isUpdating = false;
    /*
    private Vector<ShapeRenderer> frontShapes;
    private Vector<ShapeRenderer> backShapes;
    */
    
    public ScrollPanel(PanelController panelController) {
        this.panelController = panelController;
        viewportResizedListeners = new Vector<ViewportResizedListener>();
        viewportMovedListeners = new Vector<ViewportMovedListener>();
        vScrollModel = new DefaultBoundedRangeModel();
        hScrollModel = new DefaultBoundedRangeModel();
        //frontShapes = new Vector<ShapeRenderer>();
        //backShapes = new Vector<ShapeRenderer>();
        buildPanel();
        createListeners();
    }   
    
    private void buildPanel() {
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints layoutCons = new GridBagConstraints();
        setLayout(layout);
        
        innerPanel = new JPanel() {
            public void paint(Graphics g) {
               
                boolean locked = false;
                try {
                    locked = panelController.getInteractionLock().readLock().tryLock(1000, TimeUnit.MILLISECONDS);
                    if (locked) {
                    super.paint(g);
                    Graphics2D g2 = (Graphics2D)g;

                    //original commented out - gc.getInteractionLock().readLock().lock();
                        //paintBackShapes(g2);
                        panelController.getImageManager().paint(g2);
                        //paintFrontShapes(g2);
                    }
                    else {
                        
                        try {
                                              
                            /*
                            for ( StackTraceElement[] steA : Thread.getAllStackTraces().values() ) {
                                for ( StackTraceElement ste : steA ) {
                                    System.out.println(ste.toString());
                                      
                                }
                                                
                            }
                            */
                            
                        } catch ( Exception ex ) {// Catch exception if any
                            System.err.println("Error: " + ex.getMessage());
                        }
                        System.out.println("Paint lock failed");
                    }
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
                finally {
                    if (locked) {
                        panelController.getInteractionLock().readLock().unlock();
                    }
                }
            }
        };

        layoutCons.gridx = 0;
        layoutCons.gridy = 0;
        layoutCons.gridwidth = 100;
        layoutCons.gridheight = 100;
        layoutCons.fill = GridBagConstraints.BOTH;
        layoutCons.insets = new Insets(1,1,0,0);
        layoutCons.anchor = GridBagConstraints.NORTH;
        layoutCons.weightx = 120.0;
        layoutCons.weighty = 120.0;
        layout.setConstraints(innerPanel, layoutCons);
        add(innerPanel);
        
        vScrollBar = new JScrollBar(JScrollBar.VERTICAL);
        vScrollBar.setModel(vScrollModel);
        layoutCons.gridx = 100;
        layoutCons.gridy = 0;
        layoutCons.gridwidth = GridBagConstraints.REMAINDER;
        layoutCons.gridheight = 100;
        layoutCons.fill = GridBagConstraints.VERTICAL;
        layoutCons.insets = new Insets(1,0,0,1);
        layoutCons.anchor = GridBagConstraints.NORTH;
        layoutCons.weightx = 0.0;
        layoutCons.weighty = 120.0;
        layout.setConstraints(vScrollBar, layoutCons);
        add(vScrollBar);
        
        hScrollBar = new JScrollBar(JScrollBar.HORIZONTAL);
        hScrollBar.setModel(hScrollModel);
        layoutCons.gridx = 0;
        layoutCons.gridy = 100;
        layoutCons.gridwidth = 100;
        layoutCons.gridheight = GridBagConstraints.REMAINDER;
        layoutCons.fill = GridBagConstraints.HORIZONTAL;
        layoutCons.insets = new Insets(0,1,1,0);
        layoutCons.anchor = GridBagConstraints.NORTH;
        layoutCons.weightx = 120.0;
        layoutCons.weighty = 0.0;
        layout.setConstraints(hScrollBar, layoutCons);
        add(hScrollBar);
        
        JPanel cornerPanel = new JPanel();
        //cornerPanel.setBorder(new LineBorder(Color.black));
        layoutCons.gridx = 100;
        layoutCons.gridy = 100;
        layoutCons.gridwidth = GridBagConstraints.REMAINDER;
        layoutCons.gridheight = GridBagConstraints.REMAINDER;
        layoutCons.fill = GridBagConstraints.BOTH;
        layoutCons.insets = new Insets(0,0,1,1);
        layoutCons.anchor = GridBagConstraints.NORTH;
        layoutCons.weightx = 0.0;
        layoutCons.weighty = 0.0;
        layout.setConstraints(cornerPanel, layoutCons);
        add(cornerPanel);
    }

    public Dimension getViewportSize() {
        return new Dimension(innerPanel.getWidth(),innerPanel.getHeight());
    }
    
    public void addMouseListener(MouseListener ml) {
        innerPanel.addMouseListener(ml);
    }
    
    public void addMouseMotionListener(MouseMotionListener mml) {
        innerPanel.addMouseMotionListener(mml);
    }
    
    public void addMouseWheelListener(MouseWheelListener mwl) {
        innerPanel.addMouseWheelListener(mwl);
    }
    
    public void removeMouseListener(MouseListener ml) {
        innerPanel.removeMouseListener(ml);
    }
    
    public void removeMouseMotionListener(MouseMotionListener mml) {
        innerPanel.removeMouseMotionListener(mml);
    }
    
    public void removeMouseWheelListener(MouseWheelListener mwl) {
        innerPanel.removeMouseWheelListener(mwl);
    }
    
    public void enableMouseListeners() {
        
        addMouseListener(panelController.getInteractionHandler());
        addMouseMotionListener(panelController.getInteractionHandler());
        addMouseWheelListener(panelController.getInteractionHandler());
        
    }
    
    public void disableMouseListeners() {
        
        removeMouseListener(panelController.getInteractionHandler());
        removeMouseMotionListener(panelController.getInteractionHandler());
        removeMouseWheelListener(panelController.getInteractionHandler());
        
    }
    /*
    public void addBackShape(ShapeRenderer shape) {
        backShapes.add(shape);
    }
    
    public void removeBackShape(ShapeRenderer shape) {
        backShapes.remove(shape);
    }
    
    public void addFrontShape(ShapeRenderer shape) {
        frontShapes.add(shape);
    }
    
    public void removeFrontShape(ShapeRenderer shape) {
        frontShapes.remove(shape);
    }
    
    private void paintBackShapes(Graphics2D g2) {
        paintShapes(backShapes, g2);
    }
    
    private void paintFrontShapes(Graphics2D g2) {
        paintShapes(frontShapes, g2);
    }
    
    private void paintShapes(Vector<ShapeRenderer> shapes, Graphics2D g2) {
        for ( ShapeRenderer shape : shapes ) {
            shape.paint(g2);
        }
    }
    */
    protected void update(final int minX, final int maxX, final int xValue, final int xExtent,
                          final int minY, final int maxY, final int yValue, final int yExtent) {
        Runnable updater = new Runnable() {
            public void run() {
                isUpdating = true;
                hScrollModel.setRangeProperties(xValue, xExtent, minX, maxX, false);
                vScrollModel.setRangeProperties(yValue, yExtent, minY, maxY, false);
                hScrollBar.setBlockIncrement(Math.max(MIN_SCROLL_BLOCK_INCREMENT, xExtent-SCROLL_BLOCK_INCREMENT_SUBTRACT_FROM_EXTENT));
                vScrollBar.setBlockIncrement(Math.max(MIN_SCROLL_BLOCK_INCREMENT, yExtent-SCROLL_BLOCK_INCREMENT_SUBTRACT_FROM_EXTENT));
                hScrollBar.setUnitIncrement(SCROLL_UNIT_INCREMENT);
                vScrollBar.setUnitIncrement(SCROLL_UNIT_INCREMENT);
                isUpdating = false;
            }
        };
        
        SwingUtilities.invokeLater(updater);            
        
    }
    
    protected void update(final int xValue, final int yValue) {
        Runnable updater = new Runnable() {
            public void run() {
                isUpdating = true;
                hScrollModel.setValue(xValue);
                vScrollModel.setValue(yValue);
                isUpdating = false;
            }
        };            

        SwingUtilities.invokeLater(updater);            
    }
    
    private void createListeners() {
        innerPanel.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                fireViewportResized(innerPanel.getWidth(), innerPanel.getHeight());
            }
        });
        ChangeListener changeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent ce) {
                if ( !isUpdating ) {
                    fireViewportMoved(hScrollModel.getValue(),vScrollModel.getValue());
                }
            }
        };
        vScrollModel.addChangeListener(changeListener);
        hScrollModel.addChangeListener(changeListener);
    }
    
    private synchronized void fireViewportResized(int viewportWidth, int viewportHeight) {
        for ( ViewportResizedListener gvrl : viewportResizedListeners ) {
            gvrl.viewportResized(viewportWidth, viewportHeight);
        }
    }
    
    public synchronized void addViewportResizedListener(ViewportResizedListener gvrl) {
        viewportResizedListeners.add(gvrl);
    }
    
    public void removeViewportResizedListener(ViewportResizedListener gvrl) {
        viewportResizedListeners.remove(gvrl);
    }
    
    public interface ViewportResizedListener {
        public void viewportResized(int viewportWidth, int viewportHeight);
    }
    
    private void fireViewportMoved(int viewportX, int viewportY) {
        for ( ViewportMovedListener gvml : viewportMovedListeners ) {
            gvml.viewportMoved(viewportX, viewportY);
        }
    }
    
    public void addViewportMovedListener(ViewportMovedListener gvml) {
        viewportMovedListeners.add(gvml);
    }
    
    public void removeViewportMovedListener(ViewportMovedListener gvml) {
        viewportMovedListeners.remove(gvml);
    }
    
    public interface ViewportMovedListener {
        public void viewportMoved(int viewportX, int viewportY);
    }
    /*
    public static class DecoratedShape implements ShapeRenderer {
        private Shape shape;
        private Color color;
        private Stroke stroke;

        public void paint( Graphics2D g2 ) {
            g2.setColor(color);
            g2.setStroke(stroke);
            g2.draw(shape);
        }

        public DecoratedShape(Shape shape, Color color, Stroke stroke) {
            this.shape = shape;
            this.color = color;
            this.stroke = stroke;
        }

        public Shape getShape() { return shape; }

        public Color getColor() { return color; }

        public Stroke getStroke() { return stroke; }
    }
    */
}
