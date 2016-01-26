package com.sproutlife.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.ToolTipManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sproutlife.GameController;
import com.sproutlife.Settings;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.step.GameStep.StepType;
import com.sproutlife.model.step.GameStepEvent;
import com.sproutlife.model.step.GameStepListener;
import com.sproutlife.panel.gamepanel.ImageManager;
import com.sproutlife.panel.gamepanel.ScrollPanel;
import com.sproutlife.panel.gamepanel.ScrollPanel.ViewportResizedListener;
import com.sproutlife.panel.gamepanel.ScrollPanelController;
import com.sproutlife.panel.gamepanel.handler.DefaultHandlerSet;
import com.sproutlife.panel.gamepanel.handler.InteractionHandler;
import com.sproutlife.renderer.BoardRenderer;

public class PanelController {
    GameController gameController;
    GameFrame gameFrame;
 
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
        
        boardRenderer = new BoardRenderer(getGameModel());
        controlPanel = new ControlPanel(this);
        
        ScrollPanel scrollPanel = getScrollPanel();
        

        JTabbedPane rightPane = new JTabbedPane();
        rightPane.addTab("Controls", controlPanel);
        rightPane.addTab("Stats", new JPanel());

        gameFrame.getSplitPane().setLeftComponent(scrollPanel);
        gameFrame.getSplitPane().setRightComponent(rightPane);
    }
    
    private void initComponents() {
        ToolTipManager.sharedInstance().setInitialDelay(0);
        gameFrame.setVisible(true);  
        getBoardRenderer().setDefaultBlockSize(3);
        updateZoomValue(-3);
        getControlPanel().getZoomSlider().setValue(-3);        
        updateBoardSizeFromPanelSize(getScrollPanel().getViewportSize());
        getImageManager().setBackgroundColor(new Color(160,160,160)); 
        
        getControlPanel().getMaxLifespanSpinner().setValue(
                getSettings().getInt(Settings.MAX_LIFESPAN));
    }

    public void addListeners() {
        getScrollPanel().enableMouseListeners();
        
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
        
        getControlPanel().getSpeedSlider().addChangeListener(new ChangeListener() {            
            public void stateChanged(ChangeEvent e) {
                int value = ((JSlider) e.getSource()).getValue();
                updateSpeedValue(value);

            }
        });
        
        getControlPanel().getStartPauseButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (getGameModel().getPlayGame()) {
                    getGameModel().setPlayGame(false);
                    getControlPanel().getStartPauseButton().setText("Start");
                }
                else {
                    getGameModel().setPlayGame(true);
                    getControlPanel().getStartPauseButton().setText("Pause");
                }
            }
        });
        
        getControlPanel().getStepButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                getGameModel().performGameStep();
                getImageManager().repaintNewImage();
            }
        });
        
        
        getControlPanel().getResetButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                getInteractionLock().writeLock().lock();
                getGameModel().getEchosystem().resetCells();
                getInteractionLock().writeLock().unlock();
                getImageManager().repaintNewImage();
            }
        });
        
        ItemListener lifeModeListener = new ItemListener() {  
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (getControlPanel().getRdbtnFriendly().isSelected()) {
                    getSettings().set(Settings.LIFE_MODE, "friendly");
                }
                else if(getControlPanel().getRdbtnCooperative().isSelected()) {
                    getSettings().set(Settings.LIFE_MODE, "cooperative");
                }
                else {
                    getSettings().set(Settings.LIFE_MODE, "competitive");
                }
            }                      
        };        
        getControlPanel().getRdbtnCompetitive().addItemListener(lifeModeListener);
        getControlPanel().getRdbtnFriendly().addItemListener(lifeModeListener);
        getControlPanel().getRdbtnCooperative().addItemListener(lifeModeListener);
        
        
        getControlPanel().getMaxLifespanSpinner().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                getSettings().set(Settings.MAX_LIFESPAN,((JSpinner) arg0.getSource()).getValue());
            }
        });

        
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
    public void updateSpeedValue(int value) {
        getGameModel().getGameThread().setAutoAdjust(false);
   
        int sleepDelay = 1;
        int iterations = 1;
        switch (value) {
            case -5 : sleepDelay = 500; break;
            case -4 : sleepDelay = 100; break;
            case -3 : sleepDelay = 20; break;
            case -2 : sleepDelay = 8; break;
            case -1 : sleepDelay = 4; break;  
            case 0 : break;
            case 1 : iterations = 2; break;
            case 2 : iterations = 4; break;
            case 3 : iterations = 8; break;
            case 4 : iterations = 16; break;
            case 5 : iterations = 32; break;          
        }
        getGameModel().getGameThread().setSleepDelay(sleepDelay);
        getGameModel().getGameThread().setIterations(iterations);

   
    }
    
    public void updateZoomValue(int value) {
        
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
        Dimension boardSize = new Dimension(d.width/getBoardRenderer().getDefaultBlockSize()-2,d.height/getBoardRenderer().getDefaultBlockSize()-2);
        getGameModel().getEchosystem().setBoardSize(boardSize);
        
        getInteractionLock().writeLock().unlock();
        
        getScrollController().updateScrollBars();

    }    
}
