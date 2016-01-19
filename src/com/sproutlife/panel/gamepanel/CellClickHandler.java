package com.sproutlife.panel.gamepanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.sproutlife.GameController;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Board;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.step.GameStepEvent;
import com.sproutlife.model.step.GameStepListener;
import com.sproutlife.model.step.GameStep.StepType;

public class CellClickHandler {
    GameController gameController;
    MouseAdapter cellClickListener; 
    
    public CellClickHandler(GameController gameController) {
        this.gameController = gameController;
        initialize();
    }
    
    public void addToGamePanel() {
        getGamePanel().addMouseListener(cellClickListener);
        getGamePanel().addMouseMotionListener(cellClickListener);
    }
    
    private GameModel getGameModel() {
        return gameController.getGameModel();
    }
    
    private GamePanel getGamePanel() {
        return gameController.getFrame().getGamePanel();
    }
    
    private Board getBoard() {
        return getGameModel().getEchosystem().getBoard();
    }
    
    public void initialize() {
        
        cellClickListener = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                // Mouse was released (user clicked)
                addCell(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // Mouse is being dragged, user wants multiple selections
                addCell(e);
            }
            
        };
    }
    
    
    public void addCell(int x, int y) {
        /*
        if (x>getSproutLife().getBoard().getWidth() || y>getSproutLife().getBoard().getHeight()) {
            getSproutLife().getEchosystem().resetCells();
        }
        */
        if (getGameModel().getBoard().getCell(x, y)==null) {
            Cell c = getGameModel().getEchosystem().addCell(x, y);
            //getGameModel().getBoard().setCell(c);
        }
        else {
            getGameModel().getEchosystem().removeCell(x, y);
            //getGameModel().getBoard().clearCell(x, y);
        }
        
        getGamePanel().repaint();
    }
    
    public void addCell(MouseEvent me) {
        
        int x = me.getPoint().x/getGamePanel().BLOCK_SIZE-1;
        int y = me.getPoint().y/getGamePanel().BLOCK_SIZE-1;
        
        if ((x >= 0) && (x < getBoard().getWidth()) && (y >= 0) && (y < getBoard().getHeight())) {
            addCell(x,y);
        }
    }
    


}
