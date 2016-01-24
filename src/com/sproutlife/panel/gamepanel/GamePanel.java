package com.sproutlife.panel.gamepanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ConcurrentModificationException;

import javax.swing.JPanel;

import com.sproutlife.model.GameModel;
import com.sproutlife.panel.PanelController;
import com.sproutlife.panel.gamepanel.handler.CellClickHandler;
import com.sproutlife.renderer.BoardRenderer;

public class GamePanel extends JPanel {

    PanelController controller;
    BoardRenderer boardRenderer;

    public GamePanel(PanelController panelController) {
        this.controller = panelController;
    }       

    public GameModel getGameModel() {
        return controller.getGameModel();
    }
    
    public void setBoardRenderer(BoardRenderer boardRenderer) {
        this.boardRenderer = boardRenderer;
    }

    public BoardRenderer getBoardRenderer() {
        return boardRenderer;
    }

    @Override
    public void paintComponent(Graphics g) {
        
        super.paintComponent(g);

        synchronized (getGameModel().getEchosystem()) {
            try {
                getBoardRenderer().paint((Graphics2D) g);

            }
            catch (ConcurrentModificationException cme) {
            }
        }
        // Setup grid
        /*
         * g.setColor(Color.BLACK); for (int i=0; i<=d_gameBoardSize.width; i++)
         * { g.drawLine(((i*BLOCK_SIZE)+BLOCK_SIZE), BLOCK_SIZE,
         * (i*BLOCK_SIZE)+BLOCK_SIZE, BLOCK_SIZE +
         * (BLOCK_SIZE*d_gameBoardSize.height)); } for (int i=0;
         * i<=d_gameBoardSize.height; i++) { g.drawLine(BLOCK_SIZE,
         * ((i*BLOCK_SIZE)+BLOCK_SIZE), BLOCK_SIZE*(d_gameBoardSize.width+1),
         * ((i*BLOCK_SIZE)+BLOCK_SIZE)); }
         */
    }

}
