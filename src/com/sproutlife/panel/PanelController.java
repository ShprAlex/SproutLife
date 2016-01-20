package com.sproutlife.panel;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sproutlife.GameController;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.step.GameStepEvent;
import com.sproutlife.model.step.GameStepListener;
import com.sproutlife.model.step.GameStep.StepType;
import com.sproutlife.panel.handler.CellClickHandler;
import com.sproutlife.renderer.BoardRenderer;

public class PanelController {
    GameController gameController;
    GameFrame gameFrame;
    GamePanel gamePanel;    
    ControlPanel controlPanel;
    JMenuBar gameMenu;
    BoardRenderer boardRenderer;
    
    public PanelController(GameController gameController) {
        this.gameController = gameController;
        
        buildPanels();
                    
    }
    
    public void start() {
        initComponents();
        
        addListeners();
        
        updateFromSettings();
    }
    
    public GameController getGameController() {
        return gameController;
    };
    
    public GamePanel getGamePanel() {
        return gamePanel;
    }
    
    public BoardRenderer getBoardRenderer() {
        return boardRenderer;
    }
    
    public GameModel getGameModel() {
        return getGameController().getGameModel();
    }
    
    public GameFrame getGameFrame() {
        return gameFrame;
    }
    
    public ControlPanel getControlPanel() {
        return controlPanel;
    }
    
    public JMenuBar getGameMenu() {
        return gameMenu;
    }
    
    public void buildPanels() {
        gameFrame = new GameFrame(this);
        gameMenu = new GameMenu(this);
        gameFrame.setJMenuBar(gameMenu);
        
        
        gamePanel = new GamePanel(this);
        boardRenderer = new BoardRenderer(getGameModel(), gamePanel);
        gamePanel.setBoardRenderer(boardRenderer);
        controlPanel = new ControlPanel(this);

        JTabbedPane rightPane = new JTabbedPane();
        rightPane.addTab("Controls", controlPanel);
        rightPane.addTab("Stats", new JPanel());

        gameFrame.getSplitPane().setLeftComponent(gamePanel);
        gameFrame.getSplitPane().setRightComponent(rightPane);
    }
    
    private void initComponents() {
        gameFrame.setVisible(true); 
        
        updateBoardSizeFromGamePanelSize();        
    }

    public void addListeners() {
        CellClickHandler cellClickHandler = new CellClickHandler(this);
        cellClickHandler.addToGamePanel();
        
        getGamePanel().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateBoardSizeFromGamePanelSize();
            }
        });  
        
        getGameModel().setGameStepListener(new GameStepListener() {
            @Override
            public void stepPerformed(GameStepEvent event) {
                if (event.getStepType() == StepType.STEP_BUNDLE) {
                    getGamePanel().repaint();
                }
            }
        });
        
        getControlPanel().getZoomSlider().addChangeListener(new ChangeListener() {            
            public void stateChanged(ChangeEvent e) {
                int value = ((JSlider) e.getSource()).getValue();
                double zoom = Math.pow(1.1, value);
                getBoardRenderer().setZoom(zoom);  
                getGamePanel().repaint();
            }
        });
        
    }
          
    public void updateFromSettings() {
        
    }
    
    public void updateBoardSizeFromGamePanelSize() {
        synchronized (getGameModel().getEchosystem()) {    

            Dimension dimension = new Dimension(
                    getGamePanel().getWidth()/BoardRenderer.BLOCK_SIZE-2, 
                    getGamePanel().getHeight()/BoardRenderer.BLOCK_SIZE-2);
            getGameModel().getEchosystem().setBoardSize(dimension);
            getGamePanel().repaint();
        }

    }
       

    
}
