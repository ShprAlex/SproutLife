/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.swing.JComboBox;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ToolTipManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultCaret;

import com.sproutlife.GameController;
import com.sproutlife.Settings;
import com.sproutlife.action.ActionManager;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.seed.SeedFactory.SeedType;
import com.sproutlife.panel.gamepanel.ImageManager;
import com.sproutlife.panel.gamepanel.ImageManager.LogoStyle;
import com.sproutlife.panel.gamepanel.ScrollPanel;
import com.sproutlife.panel.gamepanel.ScrollPanelController;
import com.sproutlife.panel.gamepanel.handler.DefaultHandlerSet;
import com.sproutlife.panel.gamepanel.handler.InteractionHandler;
import com.sproutlife.renderer.BoardRenderer;
import com.sproutlife.renderer.colors.ColorModel.BackgroundTheme;

public class PanelController {
    GameController gameController;
    GameFrame gameFrame;
    JSplitPane splitPane;
    ActionManager actionManager;
 
    MainControlPanel mainControlPanel;
    DisplayControlPanel displayControlPanel;
    SettingsControlPanel settingsControlPanel;
    StatsPanel statsPanel;
    TipsPanel tipsPanel;
    JMenuBar gameMenu;
    GameToolbar gameToolbar;
    BoardRenderer boardRenderer;
        
    ScrollPanelController scrollController;
    ImageManager imageManager;
    InteractionHandler interactionHandler;
    BoardSizeHandler boardSizeHandler;
      
    public PanelController(GameController gameController) {
        this.gameController = gameController;
        this.actionManager = new ActionManager(this);
        this.interactionHandler = new InteractionHandler(this); 
        interactionHandler.setHandlerSet(new DefaultHandlerSet(this));

        this.scrollController = new ScrollPanelController(this);
        this.imageManager = new ImageManager(this,  LogoStyle.Small);
        this.boardSizeHandler = new BoardSizeHandler(this);

        buildPanels();
        initComponents();

        addGeneralListeners();
        addToolbarListeners();
        addMainControlPanelListeners();
        addDisplayControlPanelListeners();
        addSettingsControlPanelListeners();
        boardSizeHandler.addListeners();

        updateFromSettings();
    }

    public GameController getGameController() {
        return gameController;
    };   
    
    /**
     * @return the actionManager
     */
    public ActionManager getActionManager() {
        return actionManager;
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
    
    /**
     * @return the statsPanel
     */
    public StatsPanel getStatsPanel() {
        return statsPanel;
    }
    
    /**
     * @return the tipsPanel
     */
    public TipsPanel getTipsPanel() {
        return tipsPanel;
    }
    
    public JMenuBar getGameMenu() {
        return gameMenu;
    }
    
    public GameToolbar getGameToolbar() {
        return gameToolbar;
    }

    public Settings getSettings() {
        return getGameController().getSettings();
    }

    public BoardSizeHandler getBoardSizeHandler() {
        return boardSizeHandler;
    }

    public void buildPanels() {
        gameFrame = new GameFrame(this);
        gameMenu = new GameMenu(this);
        gameFrame.setJMenuBar(gameMenu);
        gameToolbar = new GameToolbar(this);
        
        boardRenderer = new BoardRenderer(getGameModel());
        mainControlPanel = new MainControlPanel(this);
        displayControlPanel = new DisplayControlPanel(this);
        settingsControlPanel = new SettingsControlPanel(this);
        statsPanel = new StatsPanel(this);
        tipsPanel = new TipsPanel(this);
        ScrollPanel scrollPanel = getScrollPanel();
        

        JTabbedPane rightPane = new JTabbedPane();
        rightPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        rightPane.addTab("Main", mainControlPanel);
        rightPane.addTab("Display", displayControlPanel);
        rightPane.addTab("Settings", settingsControlPanel);
        rightPane.addTab("Stats", statsPanel);
        rightPane.addTab("Tips", tipsPanel);

        JPanel gamePanel = new JPanel(new BorderLayout());
        gamePanel.add(scrollPanel, BorderLayout.CENTER);
        gamePanel.add(gameToolbar, BorderLayout.NORTH);

        splitPane = new JSplitPane();
        splitPane.setResizeWeight(1);
        splitPane.setOneTouchExpandable(true);
        splitPane.setLeftComponent(gamePanel);
        splitPane.setRightComponent(rightPane);

        gameFrame.add(splitPane);
    }
    
    private void initComponents() {
        ToolTipManager.sharedInstance().setInitialDelay(0);
        gameFrame.setVisible(true);  
        getBoardRenderer().setDefaultBlockSize(3);
        updateZoomValue(-3);
        getGameToolbar().getZoomSlider().setValue(-3);
        getBoardSizeHandler().updateBoardSizeFromImageSize(getScrollPanel().getViewportSize());
        getImageManager().setBackgroundColor(new Color(160,160,160)); 

        initSeedTypeComboBox();

        initStatsPanel();

        loadTipText();               
    }   

    private void initStatsPanel() {
        getStatsPanel().getStatsTextPane().setContentType("text/html");
        getStatsPanel().getStatsTextPane().setText(getGameModel().getStats().getDisplayText());
        //Fixes scroll issues on setText(), we want to keep the scroll position where it is
        DefaultCaret caret = (DefaultCaret) getStatsPanel().getStatsTextPane().getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
    }

    public void addGeneralListeners() {
        getScrollPanel().enableMouseListeners();

        getGameModel().setGameStepListener(new DefaultGameStepListener(this));
    }

    private void addToolbarListeners() {
        getGameToolbar().getStartPauseButton().setAction(
                getActionManager().getPlayGameAction()); 
        
        getGameToolbar().getStepButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                getInteractionLock().writeLock().lock();
                getGameModel().performGameStep();
                getInteractionLock().writeLock().unlock();
                getImageManager().repaintNewImage();
            }
        });
                
        getGameToolbar().getResetButton().setAction(
                getActionManager().getResetGameAction());

        getGameToolbar().getGifStopRecordingButton().setAction(getActionManager().getExportGifAction());

        getGameToolbar().getZoomSlider().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int value = ((JSlider) e.getSource()).getValue();
                updateZoomValue(value);

            }
        });
        
        getGameToolbar().getSpeedSlider().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int value = ((JSlider) e.getSource()).getValue();
                updateSpeedValue(value);

            }
        });
    }

    private void addMainControlPanelListeners() {
        ItemListener lifeModeListener = new ItemListener() {  
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (getMainControlPanel().getRdbtnFriendly().isSelected()) {
                    getSettings().set(Settings.LIFE_MODE, "friendly");
                }
                else if(getMainControlPanel().getRdbtnCompetitive1().isSelected()) {
                    getSettings().set(Settings.LIFE_MODE, "competitive1");
                }
                else {
                    getSettings().set(Settings.LIFE_MODE, "competitive2");
                }
            }                      
        };
        getMainControlPanel().getRdbtnFriendly().addItemListener(lifeModeListener);
        getMainControlPanel().getRdbtnCompetitive2().addItemListener(lifeModeListener);
        getMainControlPanel().getRdbtnCompetitive1().addItemListener(lifeModeListener);
        
        getMainControlPanel().getSeedTypeComboBox().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               getSettings().set(Settings.SEED_TYPE, getMainControlPanel().getSeedTypeComboBox().getSelectedItem().toString());
                
            }
        });        
    }
      
    public void addDisplayControlPanelListeners() {
        DisplayControlPanel dcp = getDisplayControlPanel();
        dcp.getChckbxCellLayer().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                getBoardRenderer().setPaintCellLayer(getDisplayControlPanel().getChckbxCellLayer().isSelected());
                getImageManager().repaintNewImage();
            }
        });
        
        dcp.getChckbxGenomeLayer().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                getBoardRenderer().setPaintGenomeLayer(getDisplayControlPanel().getChckbxGenomeLayer().isSelected());
                getImageManager().repaintNewImage();
            }
        });
        
        dcp.getChckbxOrgHeadLayer().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                getBoardRenderer().setPaintHeadLayer(getDisplayControlPanel().getChckbxOrgHeadLayer().isSelected());
                getImageManager().repaintNewImage();
            }
        });
        
        dcp.getChckbxOrgTailLayer().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                getBoardRenderer().setPaintTailLayer(getDisplayControlPanel().getChckbxOrgTailLayer().isSelected());
                getImageManager().repaintNewImage();
            }
        });
        
        dcp.getChckbxOutlineSeeds().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                getBoardRenderer().setOutlineSeeds(getDisplayControlPanel().getChckbxOutlineSeeds().isSelected());
                getImageManager().repaintNewImage();
            }
        });

        dcp.getSpinnerTailLength().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                int length = (int) ((JSpinner) arg0.getSource()).getValue();
                getBoardRenderer().getTailRenderer().setTailLength(length);
            }
        });
        
        ItemListener backgroundThemeListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (dcp.getRdbtnBackgroundWhite().isSelected()) {
                    getSettings().set(Settings.BACKGROUND_THEME, "white");
                    getBoardRenderer().getColorModel().setBackgroundTheme(BackgroundTheme.white);
                } else {
                    getSettings().set(Settings.BACKGROUND_THEME, "black");
                    getBoardRenderer().getColorModel().setBackgroundTheme(BackgroundTheme.black);
                }
                getImageManager().repaintNewImage();
            }
        };

        dcp.getRdbtnBackgroundBlack().addItemListener(backgroundThemeListener);
        dcp.getRdbtnBackgroundWhite().addItemListener(backgroundThemeListener);
    }
    
    public void addSettingsControlPanelListeners() {
        getSettingsControlPanel().getMaxLifespanSpinner().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                getSettings().set(Settings.MAX_LIFESPAN,((JSpinner) arg0.getSource()).getValue());
            }
        });
        
        getSettingsControlPanel().getTargetAgeSpinner().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                getSettings().set(Settings.TARGET_LIFESPAN,((JSpinner) arg0.getSource()).getValue());
            }
        });

        getSettingsControlPanel().getChildOneParentAgeSpinner().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                getSettings().set(Settings.CHILD_ONE_PARENT_AGE,((JSpinner) arg0.getSource()).getValue());
            }
        });
        
        getSettingsControlPanel().getChildTwoParentAgeSpinner().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                getSettings().set(Settings.CHILD_TWO_PARENT_AGE,((JSpinner) arg0.getSource()).getValue());
            }
        });
        
        getSettingsControlPanel().getChildThreeParentAgeSpinner().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                getSettings().set(Settings.CHILD_THREE_PARENT_AGE,((JSpinner) arg0.getSource()).getValue());
            }
        });
        
        getSettingsControlPanel().getMutationRateSpinner().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                getSettings().set(Settings.MUTATION_RATE,((JSpinner) arg0.getSource()).getValue());
            }
        });
        
        
        ItemListener sproutModeListener = new ItemListener() {  
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (getSettingsControlPanel().getRdbtnVisual().isSelected()) {
                    getSettings().set(Settings.SPROUT_DELAYED_MODE, true);
                }
                else if(getSettingsControlPanel().getRdbtnFunctional().isSelected()) {
                    getSettings().set(Settings.SPROUT_DELAYED_MODE, false);
                }
            }                      
        };        
        getSettingsControlPanel().getRdbtnVisual().addItemListener(sproutModeListener);
        getSettingsControlPanel().getRdbtnFunctional().addItemListener(sproutModeListener);      
    }

    public void updateFromSettings() {
        getSettingsControlPanel().getMaxLifespanSpinner().setValue(
                getSettings().getInt(Settings.MAX_LIFESPAN));
        getSettingsControlPanel().getTargetAgeSpinner().setValue(
                getSettings().getInt(Settings.TARGET_LIFESPAN));
        getSettingsControlPanel().getChildOneParentAgeSpinner().setValue(
                getSettings().getInt(Settings.CHILD_ONE_PARENT_AGE));
        getSettingsControlPanel().getChildTwoParentAgeSpinner().setValue(
                getSettings().getInt(Settings.CHILD_TWO_PARENT_AGE));
        getSettingsControlPanel().getChildThreeParentAgeSpinner().setValue(
                getSettings().getInt(Settings.CHILD_THREE_PARENT_AGE));
        getSettingsControlPanel().getMutationRateSpinner().setValue(
                getSettings().getInt(Settings.MUTATION_RATE));

        switch (getSettings().getString(Settings.LIFE_MODE)) {
            case "friendly":
                getMainControlPanel().getRdbtnFriendly().setSelected(true);
                break;
            case "competitive1":
                getMainControlPanel().getRdbtnCompetitive1().setSelected(true);
                break;
            default:
                getMainControlPanel().getRdbtnCompetitive2().setSelected(true);
        }

        switch (getSettings().getString(Settings.BACKGROUND_THEME)) {
            case "white":
                getDisplayControlPanel().getRdbtnBackgroundWhite().setSelected(true);
                break;
            default:
                getDisplayControlPanel().getRdbtnBackgroundBlack().setSelected(true);
        }

        SeedType seedType = SeedType.get(getSettings().getString(Settings.SEED_TYPE));
        JComboBox<SeedType> seedCb = (JComboBox<SeedType>) getMainControlPanel().getSeedTypeComboBox();
        if (seedType!=null) {
            seedCb.setSelectedItem(seedType);
        }
    }
    
    public void setPlayGame(boolean playGame) {
        getActionManager().getPlayGameAction().setPlayGame(playGame);
    }
    
    public void loadTipText() {
        
        try {
            URL tipsURL = TipsPanel.class.getResource("/tips/tips.html");
            getTipsPanel().getTipsTextPane().setPage(tipsURL);
        } catch (IOException e) {         
            //This error probably means that /resources/ was not added to the build path
            e.printStackTrace();
        }

    }
    
    public void initSeedTypeComboBox() {
        JComboBox<SeedType> seedCb = (JComboBox<SeedType>) getMainControlPanel().getSeedTypeComboBox();
        seedCb.addItem(SeedType.Bentline1_RPentomino);
        seedCb.addItem(SeedType.Bentline1m_RPentomino);        
        seedCb.addItem(SeedType.Bentline_U3x3);
        seedCb.addItem(SeedType.Bentline1_Plus);
        seedCb.addItem(SeedType.Square2_RPentomino);
        seedCb.addItem(SeedType.L2_RPentomino);
        seedCb.addItem(SeedType.L2B1_RPentomino);
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

        getBoardRenderer().setZoom(zoom);          
        getScrollController().setScalingZoomFactor(zoom);

        getBoardSizeHandler().updateImageWidthHeightLabel();
    }
}
