/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.swing.JComboBox;
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
import com.sproutlife.model.seed.SeedFactory.SeedType;
import com.sproutlife.model.step.GameStep.StepType;
import com.sproutlife.model.step.GameStepEvent;
import com.sproutlife.model.step.GameStepListener;
import com.sproutlife.panel.gamepanel.ImageManager;
import com.sproutlife.panel.gamepanel.ImageManager.LogoStyle;
import com.sproutlife.panel.gamepanel.ScrollPanel;
import com.sproutlife.panel.gamepanel.ScrollPanel.ViewportResizedListener;
import com.sproutlife.panel.gamepanel.ScrollPanelController;
import com.sproutlife.panel.gamepanel.handler.DefaultHandlerSet;
import com.sproutlife.panel.gamepanel.handler.InteractionHandler;
import com.sproutlife.renderer.BoardRenderer;

public class PanelController {
    GameController gameController;
    GameFrame gameFrame;
 
    MainControlPanel mainControlPanel;
    DisplayControlPanel displayControlPanel;
    SettingsControlPanel settingsControlPanel;
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

        this.imageManager = new ImageManager(this,  LogoStyle.Small);
        
        buildPanels();
            
    }
    
    public void start() {
        initComponents();
        
        addGeneralListeners();
        addMainControlPanelListeners();
        addDisplayControlPanelListeners();
        addSettingsControlPanelListeners();
        
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
    
    public MainControlPanel getMainControlPanel() {
        return mainControlPanel;
    }
    
    /**
     * @return the settingsControlPanel
     */
    public SettingsControlPanel getSettingsControlPanel() {
        return settingsControlPanel;
    }
    
    /**
     * @return the displayControlPanel
     */
    public DisplayControlPanel getDisplayControlPanel() {
        return displayControlPanel;
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
        mainControlPanel = new MainControlPanel(this);
        displayControlPanel = new DisplayControlPanel(this);
        settingsControlPanel = new SettingsControlPanel(this);
        
        ScrollPanel scrollPanel = getScrollPanel();
        

        JTabbedPane rightPane = new JTabbedPane();
        rightPane.addTab("Main", mainControlPanel);
        rightPane.addTab("Display", displayControlPanel);
        rightPane.addTab("Settings", settingsControlPanel);

        gameFrame.getSplitPane().setLeftComponent(scrollPanel);
        gameFrame.getSplitPane().setRightComponent(rightPane);
    }
    
    private void initComponents() {
        ToolTipManager.sharedInstance().setInitialDelay(0);
        gameFrame.setVisible(true);  
        getBoardRenderer().setDefaultBlockSize(3);
        updateZoomValue(-3);
        getMainControlPanel().getZoomSlider().setValue(-3);        
        updateBoardSizeFromPanelSize(getScrollPanel().getViewportSize());
        getImageManager().setBackgroundColor(new Color(160,160,160)); 
        
        getSettingsControlPanel().getMaxLifespanSpinner().setValue(
                getSettings().getInt(Settings.MAX_LIFESPAN));
        
        initSeedTypeComboBox();
    }

    public void addGeneralListeners() {
        getScrollPanel().enableMouseListeners();
        
        getScrollPanel().addViewportResizedListener(new ViewportResizedListener() {
            public void viewportResized(int viewportWidth, int viewportHeight) {
                if (getMainControlPanel().getAutoSizeGridCheckbox().isSelected()) {
                    updateBoardSizeFromPanelSize(new Dimension(viewportWidth, viewportHeight));
                } 
                                                
            }
        });
        
        getGameModel().setGameStepListener(new GameStepListener() {
            @Override
            public void stepPerformed(GameStepEvent event) {
                if (event.getStepType() == StepType.STEP_BUNDLE) {
                    getImageManager().repaintNewImage();
                }
            }
        });
                                      
    }
    
    private void addMainControlPanelListeners() {
        getMainControlPanel().getStartPauseButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (getGameModel().getPlayGame()) {
                    getGameModel().setPlayGame(false);
                    getMainControlPanel().getStartPauseButton().setText("Start");
                }
                else {
                    getGameModel().setPlayGame(true);
                    getMainControlPanel().getStartPauseButton().setText("Pause");
                }
            }
        });
        
        getMainControlPanel().getStepButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                getGameModel().performGameStep();
                getImageManager().repaintNewImage();
            }
        });
        
        
        getMainControlPanel().getResetButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                getInteractionLock().writeLock().lock();
                getGameModel().getEchosystem().resetCells();
                getInteractionLock().writeLock().unlock();
                getImageManager().repaintNewImage();
            }
        });
        
        getMainControlPanel().getZoomSlider().addChangeListener(new ChangeListener() {            
            public void stateChanged(ChangeEvent e) {
                int value = ((JSlider) e.getSource()).getValue();
                updateZoomValue(value);

            }
        });
        
        getMainControlPanel().getSpeedSlider().addChangeListener(new ChangeListener() {            
            public void stateChanged(ChangeEvent e) {
                int value = ((JSlider) e.getSource()).getValue();
                updateSpeedValue(value);

            }
        });
        
        getMainControlPanel().getBoardWidthSpinner().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {                
                getMainControlPanel().getAutoSizeGridCheckbox().setSelected(false);                
                int width =  (int) getMainControlPanel().getBoardWidthSpinner().getValue();
                int height = (int) getMainControlPanel().getBoardHeightSpinner().getValue();
                updateBoardSize(width, height);
            }
        });
        
        getMainControlPanel().getBoardHeightSpinner().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                int width =  (int) getMainControlPanel().getBoardWidthSpinner().getValue();
                int height = (int) getMainControlPanel().getBoardHeightSpinner().getValue();
                updateBoardSize(width, height);
            }
        });
        
        getMainControlPanel().getClipGridToViewButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                getBoardRenderer().setDefaultBlockSize(getBoardRenderer().getBlockSize());
                updateBoardSizeFromPanelSize(getScrollPanel().getViewportSize());                                               
            }
        });
        
        ItemListener lifeModeListener = new ItemListener() {  
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (getMainControlPanel().getRdbtnFriendly().isSelected()) {
                    getSettings().set(Settings.LIFE_MODE, "friendly");
                }
                else if(getMainControlPanel().getRdbtnCooperative().isSelected()) {
                    getSettings().set(Settings.LIFE_MODE, "cooperative");
                }
                else {
                    getSettings().set(Settings.LIFE_MODE, "competitive");
                }
            }                      
        };        
        getMainControlPanel().getRdbtnCompetitive().addItemListener(lifeModeListener);
        getMainControlPanel().getRdbtnFriendly().addItemListener(lifeModeListener);
        getMainControlPanel().getRdbtnCooperative().addItemListener(lifeModeListener);    
        
        getMainControlPanel().getSeedTypeComboBox().addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
               getSettings().set(Settings.SEED_TYPE, getMainControlPanel().getSeedTypeComboBox().getSelectedItem().toString());
                
            }
        });        
    }
   
    public void addDisplayControlPanelListeners() {
        getDisplayControlPanel().getChckbxCellLayer().addItemListener(new ItemListener() {            
            @Override
            public void itemStateChanged(ItemEvent e) {
                getBoardRenderer().setPaintCellLayer(getDisplayControlPanel().getChckbxCellLayer().isSelected());
                
            }
        });
        
        getDisplayControlPanel().getChckbxGenomeLayer().addItemListener(new ItemListener() {            
            @Override
            public void itemStateChanged(ItemEvent e) {
                getBoardRenderer().setPaintGenomeLayer(getDisplayControlPanel().getChckbxGenomeLayer().isSelected());
                
            }
        });
        
        getDisplayControlPanel().getChckbxOrgHeadLayer().addItemListener(new ItemListener() {            
            @Override
            public void itemStateChanged(ItemEvent e) {
                getBoardRenderer().setPaintHeadLayer(getDisplayControlPanel().getChckbxOrgHeadLayer().isSelected());
                
            }
        });
        
        getDisplayControlPanel().getChckbxOrgTailLayer().addItemListener(new ItemListener() {            
            @Override
            public void itemStateChanged(ItemEvent e) {
                getBoardRenderer().setPaintTailLayer(getDisplayControlPanel().getChckbxOrgTailLayer().isSelected());
                
            }
        });
    }
    
    public void addSettingsControlPanelListeners() {
        getSettingsControlPanel().getMaxLifespanSpinner().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                getSettings().set(Settings.MAX_LIFESPAN,((JSpinner) arg0.getSource()).getValue());
            }
        });         
    }
       
          
    public void updateFromSettings() {
        String lifeMode = getSettings().getString(Settings.LIFE_MODE);
        if ("friendly".equals(lifeMode)) {
            getMainControlPanel().getRdbtnFriendly().setSelected(true);
        }
        else {
            getMainControlPanel().getRdbtnCompetitive().setSelected(true);
        }
        
    }
    
    public void initSeedTypeComboBox() {
        JComboBox<SeedType> seedCb = ( JComboBox<SeedType>)getMainControlPanel().getSeedTypeComboBox();
        seedCb.addItem(SeedType.Bentline1_RPentomino);
        seedCb.addItem(SeedType.Bentline1m_RPentomino);
        seedCb.addItem(SeedType.Square2_RPentomino);
        seedCb.addItem(SeedType.L2_RPentomino);
        seedCb.addItem(SeedType.L2B1_RPentomino);
        seedCb.addItem(SeedType.Boxlid_RPentomino);
        seedCb.addItem(SeedType.Boxlid3_RPentomino);
        seedCb.addItem(SeedType.Boxhat_RPentomino);
        seedCb.addItem(SeedType.OnebitB1_RPentomino);
        seedCb.addItem(SeedType.Glider_RPentomino);
        seedCb.addItem(SeedType.RPentomino_RPentomino);
        
        
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
        //getScrollController().updateScrollBars();
        
        int imageWidth = (int) getScrollController().getRendererRectangle().getWidth();       
        int imageHeight = (int) getScrollController().getRendererRectangle().getHeight();
        getMainControlPanel().getImageWidthHeightLabel().setText(imageWidth+", "+imageHeight);
        //getImageManager().repaintNewGraphImage();
    }
    
    public void updateBoardSizeFromPanelSize(Dimension d) {
        //d.width-=40;
        //d.height-=40;

        getInteractionLock().writeLock().lock();
         
        getBoardRenderer().setBounds(d);
                
        int boardWidth = (d.width-40)/getBoardRenderer().getDefaultBlockSize()-2;
        int boardHeight = (d.height-40)/getBoardRenderer().getDefaultBlockSize()-2;
        
        boolean autoSizeGrid = getMainControlPanel().getAutoSizeGridCheckbox().isSelected();
        if (autoSizeGrid) {
            getMainControlPanel().getBoardWidthSpinner().setValue(boardWidth);
            getMainControlPanel().getBoardHeightSpinner().setValue(boardHeight);
        }
        
        getMainControlPanel().getAutoSizeGridCheckbox().setSelected(autoSizeGrid);

        Dimension boardSize = new Dimension(boardWidth,boardHeight);
        getGameModel().getEchosystem().setBoardSize(boardSize);

        getInteractionLock().writeLock().unlock();

        getScrollController().updateScrollBars();
        
        int imageWidth = (int) getScrollController().getRendererRectangle().getWidth();       
        int imageHeight = (int) getScrollController().getRendererRectangle().getHeight();
        getMainControlPanel().getImageWidthHeightLabel().setText(imageWidth+", "+imageHeight);
        
        getImageManager().repaintNewImage();

    }    
    
    public void updateBoardSize(int width, int height) {
        int displayWidth = (width+2)*getBoardRenderer().getDefaultBlockSize()+40;
        int displayHeight = (height+2)*getBoardRenderer().getDefaultBlockSize()+40;
        updateBoardSizeFromPanelSize(new Dimension(displayWidth,displayHeight));
    }
}
