package com.sproutlife.panel.gamepanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ConcurrentModificationException;

import javax.swing.JPanel;

import com.sproutlife.GameController;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.step.GameStep.StepType;
import com.sproutlife.model.step.GameStepEvent;
import com.sproutlife.model.step.GameStepListener;
import com.sproutlife.renderer.BoardRenderer;


public class GamePanel extends JPanel  {
    public static final int BLOCK_SIZE = 5;
  
    GameController controller;
    BoardRenderer boardRenderer;       
    
    public GamePanel(GameController controller) {
        // Add resizing listener
        this.controller = controller;
        this.boardRenderer = new BoardRenderer(getGameModel(), this);
        
        addResizeListener();
        addGameStepListener();        
    }
    
    public void addHandlers() {
        CellClickHandler cellClickHandler = new CellClickHandler(controller);
        cellClickHandler.addToGamePanel();               
    }
     
    private GameModel getGameModel() {
        return controller.getGameModel();
    }
    
    private BoardRenderer getBoardRenderer() {
    	return boardRenderer;
    }
    
    private void addGameStepListener() {    
        
        GameStepListener gameStepListener = new GameStepListener() {
            
            @Override
            public void stepPerformed(GameStepEvent event) {
                if (event.getStepType()==StepType.STEP_BUNDLE) {
                    repaint();
                }                
            }
        };
        getGameModel().setGameStepListener(gameStepListener);
    }
    
    private void addResizeListener() {
        ComponentAdapter resizeLisnener = new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Setup the game board size with proper boundries
                synchronized (getGameModel().getEchosystem()) {
                    updateBoardSizeFromPanelSize();
                }
                //updateArraySize();
                
                repaint();
            }
        };
        addComponentListener(resizeLisnener);
    }
    
    public void updateBoardSizeFromPanelSize() {
        Dimension dimension = new Dimension(
                getWidth()/BLOCK_SIZE-2, 
                getHeight()/BLOCK_SIZE-2);
        getGameModel().getEchosystem().setBoardSize(dimension);
    }
       
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        /*
        AffineTransform t = new AffineTransform();
        t.setToScale(2, 2);
        ((Graphics2D) g).setTransform(t);
        */
       
        
        synchronized (getGameModel().getEchosystem()) {
        	try {
        		getBoardRenderer().paint((Graphics2D) g);

        	} catch (ConcurrentModificationException cme) {}
        }
        // Setup grid
        /*
        g.setColor(Color.BLACK);
        for (int i=0; i<=d_gameBoardSize.width; i++) {
            g.drawLine(((i*BLOCK_SIZE)+BLOCK_SIZE), BLOCK_SIZE, (i*BLOCK_SIZE)+BLOCK_SIZE, BLOCK_SIZE + (BLOCK_SIZE*d_gameBoardSize.height));
        }
        for (int i=0; i<=d_gameBoardSize.height; i++) {
            g.drawLine(BLOCK_SIZE, ((i*BLOCK_SIZE)+BLOCK_SIZE), BLOCK_SIZE*(d_gameBoardSize.width+1), ((i*BLOCK_SIZE)+BLOCK_SIZE));
        }
        */
    }

}
