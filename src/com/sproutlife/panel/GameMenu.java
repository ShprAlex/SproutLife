/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * Development started with a Java based implementation created by Matthew Burke.
 * http://burke9077.com 
 * Burke9077@gmail.com @burke9077 Creative Commons Attribution 4.0 International
 *******************************************************************************/
package com.sproutlife.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import com.sproutlife.Settings;
import com.sproutlife.action.ExportPngAction;
import com.sproutlife.action.LoadGenomeAction;
import com.sproutlife.action.SaveGenomeAction;
import com.sproutlife.model.GameModel;
import com.sproutlife.panel.gamepanel.ScrollPanel;

public class GameMenu extends JMenuBar implements ActionListener {
    PanelController controller;
    
    private JMenu fileMenu;
    private JMenu gameMenu;
    private JMenuItem exitMenuItem;
    private JMenuItem stepGameMenuItem;
    
    private Action enableMutationAction;
    
    public GameMenu(PanelController controller) {
        this.controller = controller;
        initActions();
        initMenu();
    }
    
    private GameModel getGameModel() {
        return controller.getGameModel();
    }    
    
    public ScrollPanel getScrollPanel() {
        return controller.getScrollPanel();
    }

    private void initMenu() {
        fileMenu = new JMenu("File");
        this.add(fileMenu);
        gameMenu = new JMenu("Game");
        this.add(gameMenu);

        exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(this);
        fileMenu.add(new SaveGenomeAction(controller));
        fileMenu.add(controller.getActionManager().getLoadGenomeAction());
        fileMenu.add(new ExportPngAction(controller));
        fileMenu.add(controller.getActionManager().getExportGifAction());
        fileMenu.add(new JSeparator());
        fileMenu.add(exitMenuItem);

        stepGameMenuItem = new JMenuItem("Step");
        stepGameMenuItem.addActionListener(this);

        gameMenu.add(controller.getActionManager().getPlayGameAction());
        gameMenu.add(stepGameMenuItem);
        gameMenu.add(controller.getActionManager().getResetGameAction());
        gameMenu.add(this.enableMutationAction);
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(exitMenuItem)) {
            System.exit(0);
        } else if (ae.getSource().equals(stepGameMenuItem)) {
            controller.getInteractionLock().writeLock().lock();
            getGameModel().performGameStep();
            controller.getInteractionLock().writeLock().unlock();
            controller.getImageManager().repaintNewImage();
        } 
    }
    
    private void initActions() {
        this.enableMutationAction = new AbstractAction("Disable Mutations") {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean enabled = getGameModel().getSettings().getBoolean(Settings.MUTATION_ENABLED);               
                getGameModel().getSettings().set(Settings.MUTATION_ENABLED,!enabled);
                enabled=!enabled;
                if (enabled) {
                    this.putValue(NAME, "Disable Mutations");                    
                }
                else {
                    this.putValue(NAME, "Enable Mutations");  
                }
            }
        };
    }
}
