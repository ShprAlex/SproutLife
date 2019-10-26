package com.sproutlife.panel;

import java.awt.Dimension;
import java.awt.Rectangle;

public class BoardSizeHandler {
    PanelController pc;

    public BoardSizeHandler(PanelController panelController) {
        this.pc = panelController;
    }

    public void updateBoardSizeFromImageSize(Dimension d) {
        updateBoardSizeFromImageSize(new Rectangle(0, 0, d.width,d.height));
    }

    public void updateBoardSizeFromImageSize(Rectangle r) {
        pc.getInteractionLock().writeLock().lock();
        int boardWidth = (r.width-40)/pc.getBoardRenderer().getDefaultBlockSize()-2;
        int boardHeight = (r.height-40)/pc.getBoardRenderer().getDefaultBlockSize()-2;
        int x = r.x/pc.getBoardRenderer().getDefaultBlockSize();
        int y = r.y/pc.getBoardRenderer().getDefaultBlockSize();

        boardWidth=Math.max(1, boardWidth);
        boardHeight=Math.max(1, boardHeight);
        pc.getGameModel().getEchosystem().updateBoard(new Rectangle(x,y,boardWidth, boardHeight));
        
        pc.getBoardRenderer().setBounds(new Dimension(r.width, r.height));
        boolean autoSizeGrid = pc.getMainControlPanel().getAutoSizeGridCheckbox().isSelected();
        if (autoSizeGrid) {
            pc.getMainControlPanel().getBoardWidthSpinner().setValue(boardWidth);
            pc.getMainControlPanel().getBoardHeightSpinner().setValue(boardHeight);
        }
        pc.getMainControlPanel().getAutoSizeGridCheckbox().setSelected(autoSizeGrid);

        pc.getInteractionLock().writeLock().unlock();

        pc.getScrollController().updateScrollBars();
        
        updateImageWidthHeightLabel();
        
        pc.getImageManager().repaintNewImage();
    }    
    
    public void updateBoardSize(int width, int height) {
        int displayWidth = (width+2)*pc.getBoardRenderer().getDefaultBlockSize()+40;
        int displayHeight = (height+2)*pc.getBoardRenderer().getDefaultBlockSize()+40;
        updateBoardSizeFromImageSize(new Dimension(displayWidth,displayHeight));
    }
    
    public void updateImageWidthHeightLabel() {
        int imageWidth = (int) pc.getScrollController().getRendererRectangle().getWidth();       
        int imageHeight = (int) pc.getScrollController().getRendererRectangle().getHeight();
        imageWidth = Math.min(imageWidth, pc.getScrollPanel().getViewportSize().width);
        imageHeight = Math.min(imageHeight, pc.getScrollPanel().getViewportSize().height);
        
        pc.getMainControlPanel().getImageWidthHeightLabel().setText(imageWidth+", "+imageHeight);
    }
}
