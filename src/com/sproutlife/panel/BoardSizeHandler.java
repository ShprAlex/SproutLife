package com.sproutlife.panel;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sproutlife.panel.gamepanel.ScrollPanel.ViewportResizedListener;

public class BoardSizeHandler {
    PanelController pc;
    boolean updatingSize = false;
    boolean updatingBoardSizeSpinner = false;
    boolean updatingImageSizeSpinner = false;
    
    public BoardSizeHandler(PanelController panelController) {
        this.pc = panelController;
    }

    public void addListeners() {
        ChangeListener boardSizeSpinnerListener = new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                if(!updatingSize) {
                    pc.getMainControlPanel().getAutoSizeGridCheckbox().setSelected(false);
                }
                if (updatingBoardSizeSpinner) {
                    System.out.println("duplicate board spinner");
                }
                updatingBoardSizeSpinner = true;
                int width =  (int) pc.getMainControlPanel().getBoardWidthSpinner().getValue();
                int height = (int) pc.getMainControlPanel().getBoardHeightSpinner().getValue();
                updateBoardSize(width, height);
                updatingBoardSizeSpinner = false;
            }
        };
        
        ChangeListener imageSizeSpinnerListener = new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                if(!updatingSize) {
                    pc.getMainControlPanel().getAutoSizeGridCheckbox().setSelected(false);
                }
                if (updatingImageSizeSpinner) {
                    System.out.println("duplicate image spinner");
                }
                updatingImageSizeSpinner = true;
                int width =  (int) pc.getMainControlPanel().getImageWidthSpinner().getValue();
                int height = (int) pc.getMainControlPanel().getImageHeightSpinner().getValue();
                double zoom = pc.getBoardRenderer().getZoom();
                width = (int) (width*pc.getBoardRenderer().getDefaultBlockSize()/pc.getBoardRenderer().getBlockSize()/zoom);
                height = (int) (height*pc.getBoardRenderer().getDefaultBlockSize()/pc.getBoardRenderer().getBlockSize()/zoom);
                updateBoardSizeFromImageSize(new Dimension(width, height));
                updatingImageSizeSpinner = false;
            }
        };

        pc.getScrollPanel().addViewportResizedListener(new ViewportResizedListener() {
            public void viewportResized(int viewportWidth, int viewportHeight) {
                if (pc.getMainControlPanel().getAutoSizeGridCheckbox().isSelected()) {
                    boolean autoSizeGrid = pc.getMainControlPanel().getAutoSizeGridCheckbox().isSelected();
                    double zoom = pc.getBoardRenderer().getZoom();
                    int width = (int) (viewportWidth*pc.getBoardRenderer().getDefaultBlockSize()/pc.getBoardRenderer().getBlockSize()/zoom);
                    int height = (int)(viewportHeight*pc.getBoardRenderer().getDefaultBlockSize()/pc.getBoardRenderer().getBlockSize()/zoom);
                    if (autoSizeGrid) {
                        updateBoardSizeFromImageSize(
                                new Dimension(width, height));
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
                //int dbs = pc.getBoardRenderer().getDefaultBlockSize();
                //pc.getBoardRenderer().setDefaultBlockSize(pc.getBoardRenderer().getBlockSize());
                //Rectangle2D boundsD = pc.getScrollController().getRendererRectangle();//.getWidth()pc.getScrollPanel().getViewportRectangle();
                //Rectangle bounds = new Rectangle((int) boundsD.getX(),(int) boundsD.getY(),(int) boundsD.getWidth(),(int) boundsD.getHeight());
                Rectangle bounds = pc.getScrollPanel().getViewportRectangle();
                double zoom = pc.getBoardRenderer().getZoom();
                System.out.println(bounds.toString());
                bounds.width=(int) (bounds.width*pc.getBoardRenderer().getDefaultBlockSize()/pc.getBoardRenderer().getBlockSize()/zoom);
                bounds.height=(int) (bounds.height*pc.getBoardRenderer().getDefaultBlockSize()/pc.getBoardRenderer().getBlockSize()/zoom);
                bounds.x=(int) (bounds.x*pc.getBoardRenderer().getDefaultBlockSize()/pc.getBoardRenderer().getBlockSize()/zoom)+20;
                bounds.y=(int) (bounds.y*pc.getBoardRenderer().getDefaultBlockSize()/pc.getBoardRenderer().getBlockSize()/zoom)+20;
                //bounds.x+=20;
                //bounds.y+=20;
                updateBoardSizeFromImageSize(bounds);
                //pc.getBoardRenderer().setDefaultBlockSize(dbs);
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

        double blockSize = pc.getBoardRenderer().getDefaultBlockSize();
        int boardWidth = (int)((r.width-40)/blockSize);
        int boardHeight = (int)((r.height-40)/blockSize);
        int x = (int)(r.x/blockSize);
        int y = (int)(r.y/blockSize);

        boardWidth=Math.max(1, boardWidth);
        boardHeight=Math.max(1, boardHeight);
        pc.getGameModel().getEchosystem().updateBoard(new Rectangle(x,y,boardWidth, boardHeight));
        
        pc.getBoardRenderer().setBounds(new Dimension(r.width, r.height));
        
        if (!updatingBoardSizeSpinner) {
            pc.getMainControlPanel().getBoardWidthSpinner().setValue(boardWidth);
            pc.getMainControlPanel().getBoardHeightSpinner().setValue(boardHeight);
        }
        //pc.getMainControlPanel().getAutoSizeGridCheckbox().setSelected(autoSizeGrid);

        pc.getInteractionLock().writeLock().unlock();

        pc.getScrollController().updateScrollBars();
        if (!updatingImageSizeSpinner) {
            updateImageWidthHeightLabel();
        }
        
        pc.getImageManager().repaintNewImage();
        updatingSize = false;
    }    
    
    public void updateBoardSize(int width, int height) {
        double zoom = pc.getBoardRenderer().getZoom();
        int displayWidth = (int) ((width)*pc.getBoardRenderer().getDefaultBlockSize()+40);
        int displayHeight = (int) ((height)*pc.getBoardRenderer().getDefaultBlockSize()+40);
        //pc.getBoardRenderer().setBounds(new Dimension(displayWidth, displayHeight));
        updateBoardSizeFromImageSize(new Dimension(displayWidth,displayHeight));
    }
    
    public void updateImageWidthHeightLabel() {
        //boolean updateWasInProgress = updatingSize;
        //updatingSize = true;
        int imageWidth = (int) pc.getScrollController().getRendererRectangle().getWidth();       
        int imageHeight = (int) pc.getScrollController().getRendererRectangle().getHeight();
        //imageWidth = Math.min(imageWidth, pc.getScrollPanel().getViewportSize().width);
        //imageHeight = Math.min(imageHeight, pc.getScrollPanel().getViewportSize().height);
        pc.getMainControlPanel().getImageWidthSpinner().setValue(imageWidth);
        pc.getMainControlPanel().getImageHeightSpinner().setValue(imageHeight);
        //updatingSize = updateWasInProgress;
        //pc.getMainControlPanel().getImageWidthHeightLabel().setText(imageWidth+", "+imageHeight);
    }
}
