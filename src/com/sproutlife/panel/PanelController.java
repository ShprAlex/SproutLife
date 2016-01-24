package com.sproutlife.panel;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sproutlife.GameController;
import com.sproutlife.Settings;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.step.GameStep.StepType;
import com.sproutlife.model.step.GameStepEvent;
import com.sproutlife.model.step.GameStepListener;
import com.sproutlife.panel.gamepanel.GamePanel;
import com.sproutlife.panel.gamepanel.ScrollPanelController;
import com.sproutlife.panel.gamepanel.ScrollPanel;
import com.sproutlife.panel.gamepanel.ScrollPanel.ViewportResizedListener;
import com.sproutlife.panel.gamepanel.ImageManager;
import com.sproutlife.panel.gamepanel.handler.CellClickHandler;
import com.sproutlife.panel.gamepanel.handler.DefaultHandlerSet;
import com.sproutlife.panel.gamepanel.handler.InteractionHandler;
import com.sproutlife.renderer.BoardRenderer;

public class PanelController {
    GameController gameController;
    GameFrame gameFrame;
    GamePanel gamePanel;    
    ControlPanel controlPanel;
    JMenuBar gameMenu;
    BoardRenderer boardRenderer;
        
    ScrollPanelController scrollController;
    ImageManager imageManager;
    InteractionHandler interactionHandler;
      
    public PanelController(GameController gameController) {
        this.gameController = gameController;
        
        this.interactionHandler = new InteractionHandler(this); 
        interactionHandler.setHandlerSet(new DefaultHandlerSet(this));
        
        this.scrollController = new ScrollPanelController(this);

        this.imageManager = new ImageManager(this,  null);
        
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
    
    public ScrollPanel getScrollPanel() {
        return getScrollController().getScrollPanel();
    }
    
    public ScrollPanelController getScrollController() {
        return scrollController;
    }
    
    public ImageManager getImageManager() {
        return imageManager;
    }
    
    public InteractionHandler getInteractionHandler() {
        return interactionHandler;
    }
    
    public ReentrantReadWriteLock getInteractionLock() {
        return getGameController().getInteractionLock();
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
    
    public Settings getSettings() {
        return getGameController().getSettings();
    }
    
    public void buildPanels() {
        gameFrame = new GameFrame(this);
        gameMenu = new GameMenu(this);
        gameFrame.setJMenuBar(gameMenu);
        
        
        gamePanel = new GamePanel(this);
        boardRenderer = new BoardRenderer(getGameModel());
        gamePanel.setBoardRenderer(boardRenderer);
        controlPanel = new ControlPanel(this);
        
        ScrollPanel scrollPanel = getScrollPanel();
        

        JTabbedPane rightPane = new JTabbedPane();
        rightPane.addTab("Controls", controlPanel);
        rightPane.addTab("Stats", new JPanel());

        gameFrame.getSplitPane().setLeftComponent(scrollPanel);
        gameFrame.getSplitPane().setRightComponent(rightPane);
    }
    
    private void initComponents() {
        gameFrame.setVisible(true);  
        getBoardRenderer().setDefaultBlockSize(3);
        updateZoomValue(-3);
        getControlPanel().getZoomSlider().setValue(-3);
        updateBoardSizeFromPanelSize(getScrollPanel().getViewportSize());
                     
    }

    public void addListeners() {
        CellClickHandler cellClickHandler = new CellClickHandler(this);
        cellClickHandler.addToGamePanel();
        
        getScrollPanel().addViewportResizedListener(new ViewportResizedListener() {
            public void viewportResized(int viewportWidth, int viewportHeight) {
                updateBoardSizeFromPanelSize(new Dimension(viewportWidth, viewportHeight));                                
            }
        });
        
        /*
        getGamePanel().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateBoardSizeFromGamePanelSize();
            }
        });  
        */
        getGameModel().setGameStepListener(new GameStepListener() {
            @Override
            public void stepPerformed(GameStepEvent event) {
                if (event.getStepType() == StepType.STEP_BUNDLE) {
                    getImageManager().repaintNewImage();
                }
            }
        });
        
        getControlPanel().getZoomSlider().addChangeListener(new ChangeListener() {            
            public void stateChanged(ChangeEvent e) {
                int value = ((JSlider) e.getSource()).getValue();
                updateZoomValue(value);

            }
        });
        
        //Only need to add this to one of the two buttons
        getControlPanel().getRdbtnFriendly().addItemListener(new ItemListener() {  
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (getControlPanel().getRdbtnFriendly().isSelected()) {
                    getSettings().set(Settings.LIFE_MODE, "friendly");
                }
                else {
                    getSettings().set(Settings.LIFE_MODE, "competitive");
                }
            }                      
        });

        getScrollPanel().enableMouseListeners();
    }
          
    public void updateFromSettings() {
        String lifeMode = getSettings().getString(Settings.LIFE_MODE);
        if ("friendly".equals(lifeMode)) {
            getControlPanel().getRdbtnFriendly().setSelected(true);
        }
        else {
            getControlPanel().getRdbtnCompetitive().setSelected(true);
        }
        
    }
    
    public void updateZoomValue(int value) {
        
        /*
        switch (value) {
            case -5 : zoom = 1/6.0; break;
            case -4 : zoom = 2/6.0; break;
            case -3 : zoom = 3/6.0; break;
            case -2 : zoom = 4/6.0; break;
            case -1 : zoom = 5/6.0; break;
            case 0: zoom = 1; break;
            default: zoom = Math.pow(1.1, value);
        }
        */
        
        double zoom =1;
        if (value >=0 ) {
            zoom = Math.pow(1.2, value);
            getBoardRenderer().setBlockSize(6);
        }
        else {
            switch (value) {
                case -5 : getBoardRenderer().setBlockSize(1); break;
                case -4 : getBoardRenderer().setBlockSize(2); break;
                case -3 : getBoardRenderer().setBlockSize(3); break;
                case -2 : getBoardRenderer().setBlockSize(4); break;
                case -1 : getBoardRenderer().setBlockSize(5); break;                        
            }               
        }
        //updateBoardSizeFromPanelSize(getScrollPanel().getViewportSize());
        //double zoom = Math.pow(1.1, value);
        getBoardRenderer().setZoom(zoom);  
        getScrollController().setScalingZoomFactor(zoom);
        //getImageManager().repaintNewGraphImage();
    }
    
    public void updateBoardSizeFromPanelSize(Dimension d) {
        d.width-=40;
        d.height-=40;
        getInteractionLock().writeLock().lock();
        
        getBoardRenderer().setBounds(d);
        Dimension boardSize = new Dimension(d.width/getBoardRenderer().getBlockSize(),d.height/getBoardRenderer().getBlockSize());
        getGameModel().getEchosystem().setBoardSize(boardSize);
        
        getInteractionLock().writeLock().unlock();
        
        getScrollController().updateScrollBars();

    }    
}
