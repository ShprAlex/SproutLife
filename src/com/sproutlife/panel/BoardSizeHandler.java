package com.sproutlife.panel;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Rectangle2D;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sproutlife.panel.gamepanel.ScrollPanel.ViewportResizedListener;
import com.sproutlife.renderer.Dimension2Double;

public class BoardSizeHandler {
    PanelController pc;
    boolean updatingSize = false;
    ChangeListener boardSizeSpinnerListener;
    ChangeListener imageSizeSpinnerListener;

    public BoardSizeHandler(PanelController panelController) {
        this.pc = panelController;
    }

    public void addListeners() {
        boardSizeSpinnerListener = new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                pc.getMainControlPanel().getAutoSizeGridCheckbox().setSelected(false);
                int width = (int) pc.getMainControlPanel().getBoardWidthSpinner().getValue();
                int height = (int) pc.getMainControlPanel().getBoardHeightSpinner().getValue();
                updateBoardSize(width, height);
            }
        };

        imageSizeSpinnerListener = new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                pc.getMainControlPanel().getAutoSizeGridCheckbox().setSelected(false);
                int width =  (int) pc.getMainControlPanel().getImageWidthSpinner().getValue();
                int height = (int) pc.getMainControlPanel().getImageHeightSpinner().getValue();
                updateBoardSizeFromImageSize(new Dimension(width, height));
            }
        };

        pc.getScrollPanel().addViewportResizedListener(new ViewportResizedListener() {
            public void viewportResized(int viewportWidth, int viewportHeight) {
                if (pc.getMainControlPanel().getAutoSizeGridCheckbox().isSelected()) {
                    boolean autoSizeGrid = pc.getMainControlPanel().getAutoSizeGridCheckbox().isSelected();
                    if (autoSizeGrid) {
                        clipToView();
                        //updateBoardSizeFromImageSize(new Dimension(viewportWidth, viewportHeight));
                    }
                }
            }
        });

        pc.getMainControlPanel().getBoardWidthSpinner().addChangeListener(boardSizeSpinnerListener);
        pc.getMainControlPanel().getBoardHeightSpinner().addChangeListener(boardSizeSpinnerListener);
        pc.getMainControlPanel().getImageWidthSpinner().addChangeListener(imageSizeSpinnerListener);
        pc.getMainControlPanel().getImageHeightSpinner().addChangeListener(imageSizeSpinnerListener);

        pc.getMainControlPanel().getClipGridToViewButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                clipToView();
            }
        });

        pc.getMainControlPanel().getAutoSizeGridCheckbox().addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if(pc.getMainControlPanel().getAutoSizeGridCheckbox().isSelected()) {
                    clipToView();
                }
            }
        });
    }

    public void updateBoardSizeFromImageSize(Dimension d) {
        updateBoardSizeFromImageSize(new Rectangle(0, 0, d.width,d.height));
    }

    public void updateBoardSizeFromImageSize(Rectangle r) {
        if (updatingSize) {
            return;
        }

        updatingSize = true;
        pc.getInteractionLock().writeLock().lock();
        double zoom = pc.getBoardRenderer().getZoom();
        double blockSize = pc.getBoardRenderer().getBlockSize();
        int boardWidth = (int)((r.width/zoom-40)/blockSize);
        int boardHeight = (int)((r.height/zoom-40)/blockSize);
        int x = (int)(r.x/blockSize/zoom);
        int y = (int)(r.y/blockSize/zoom);

        boardWidth=Math.max(1, boardWidth);
        boardHeight=Math.max(1, boardHeight);
        pc.getGameModel().getEchosystem().updateBoard(new Rectangle(x,y,boardWidth, boardHeight));

        pc.getBoardRenderer().setBounds(new Dimension2Double(r.width/zoom, r.height/zoom));

        updateBoardSizeSpinners(boardWidth, boardHeight);
        updateImageSizeSpinners(r.width, r.height);
        pc.getInteractionLock().writeLock().unlock();

        pc.getScrollController().updateScrollBars();
        pc.getImageManager().repaintNewImage();
        updatingSize = false;
    }    
    
    public void updateBoardSize(int width, int height) {
        // we repeat most of the updateBoardSizeFromImageSize() logic in this function to avoid rounding errors.
        double zoom = pc.getBoardRenderer().getZoom();
        int displayWidth = (int) ((width)*pc.getBoardRenderer().getBlockSize()+40);
        int displayHeight = (int) ((height)*pc.getBoardRenderer().getBlockSize()+40);

        if (updatingSize) {
            return;
        }

        updatingSize = true;
        pc.getInteractionLock().writeLock().lock();

        int boardWidth=Math.max(1, width);
        int boardHeight=Math.max(1, height);
        pc.getGameModel().getEchosystem().updateBoard(new Rectangle(0,0,boardWidth, boardHeight));
        pc.getBoardRenderer().setBounds(new Dimension2Double(displayWidth, displayHeight));

        updateBoardSizeSpinners(width, height);
        updateImageSizeSpinners((int) (displayWidth*zoom), (int) (displayHeight*zoom));
        pc.getInteractionLock().writeLock().unlock();

        pc.getScrollController().updateScrollBars();
        pc.getImageManager().repaintNewImage();
        updatingSize = false;
    }

    public void updateBoardSizeSpinners(int boardWidth, int boardHeight) {
        pc.getMainControlPanel().getBoardWidthSpinner().removeChangeListener(boardSizeSpinnerListener);
        pc.getMainControlPanel().getBoardHeightSpinner().removeChangeListener(boardSizeSpinnerListener);

        pc.getMainControlPanel().getBoardWidthSpinner().setValue(boardWidth);
        pc.getMainControlPanel().getBoardHeightSpinner().setValue(boardHeight);

        pc.getMainControlPanel().getBoardWidthSpinner().addChangeListener(boardSizeSpinnerListener);
        pc.getMainControlPanel().getBoardHeightSpinner().addChangeListener(boardSizeSpinnerListener);
    }

    public void updateImageSizeSpinners(int width, int height) {
        pc.getMainControlPanel().getImageWidthSpinner().removeChangeListener(imageSizeSpinnerListener);
        pc.getMainControlPanel().getImageHeightSpinner().removeChangeListener(imageSizeSpinnerListener);

        pc.getMainControlPanel().getImageWidthSpinner().setValue(width);
        pc.getMainControlPanel().getImageHeightSpinner().setValue(height);

        pc.getMainControlPanel().getImageWidthSpinner().addChangeListener(imageSizeSpinnerListener);
        pc.getMainControlPanel().getImageHeightSpinner().addChangeListener(imageSizeSpinnerListener);
    }

    public void updateZoomValue(int value) {
        double zoom =1;
        if (value >=0 ) {
            zoom = Math.pow(1.2, value);
            pc.getBoardRenderer().setBlockSize(6);
        }
        else {
            switch (value) {
                case -5 : pc.getBoardRenderer().setBlockSize(1); break;
                case -4 : pc.getBoardRenderer().setBlockSize(2); break;
                case -3 : pc.getBoardRenderer().setBlockSize(3); break;
                case -2 : pc.getBoardRenderer().setBlockSize(4); break;
                case -1 : pc.getBoardRenderer().setBlockSize(5); break;
            }
        }
        pc.getBoardRenderer().setZoom(zoom);
        pc.getScrollController().setScalingZoomFactor(zoom);
        Rectangle2D bounds = pc.getScrollController().getRendererRectangle();
        updateImageSizeSpinners((int)bounds.getWidth(), (int)bounds.getHeight());
    }

    private void clipToView() {
        Rectangle bounds = pc.getScrollPanel().getViewportRectangle();
        double zoom = pc.getBoardRenderer().getZoom();
        System.out.println(bounds.toString());
        bounds.x+=20*zoom;
        bounds.y+=20*zoom;
        updateBoardSizeFromImageSize(bounds);
    }
}
