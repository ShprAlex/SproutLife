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
import com.sproutlife.action.ExportGifAction;
import com.sproutlife.action.ExportPngAction;
import com.sproutlife.model.GameModel;
import com.sproutlife.panel.gamepanel.ScrollPanel;

public class GameMenu extends JMenuBar implements ActionListener {
    PanelController controller;
    
    //private JMenuBar mb_menu;
    private JMenu m_file, m_game;
    private JMenuItem mi_file_exit;
    private JMenuItem mi_game_play, mi_game_step, mi_game_stop, mi_game_reset;
    
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
        // Setup menu
        //mb_menu = new JMenuBar();
        
        m_file = new JMenu("File");
        this.add(m_file);
        m_game = new JMenu("Game");
        this.add(m_game);
        //m_help = new JMenu("Help");
        //this.add(m_help);
        mi_file_exit = new JMenuItem("Exit");
        mi_file_exit.addActionListener(this);
        m_file.add(new ExportPngAction(controller));
        m_file.add(new ExportGifAction(controller));
        m_file.add(new JSeparator());
        m_file.add(mi_file_exit);

        mi_game_play = new JMenuItem(controller.getActionManager().getPlayGameAction());                        
        mi_game_step = new JMenuItem("Step");
        mi_game_step.addActionListener(this);
        mi_game_reset = new JMenuItem(controller.getActionManager().getResetGameAction());


        m_game.add(new JSeparator());
        m_game.add(mi_game_play);
        m_game.add(mi_game_step);
        m_game.add(new JMenuItem(this.enableMutationAction));
        m_game.add(mi_game_reset);

    }
    
    public void setPlayGame(boolean playGame) {
        getGameModel().setPlayGame(playGame);
        
        if (playGame) {
            mi_game_play.setEnabled(false);
            mi_game_stop.setEnabled(true);                        
 
        } else {
            mi_game_play.setEnabled(true);
            mi_game_stop.setEnabled(false);        
        }
    }        
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(mi_file_exit)) {
            // Exit the game
            System.exit(0);
        } else if (ae.getSource().equals(mi_game_step)) {
            getGameModel().performGameStep();
            getScrollPanel().repaint();
        } 
    }
    
    private void initActions() {
        this.enableMutationAction = new AbstractAction("Disable Mutations") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean enabled = getGameModel().getSettings().getBoolean(Settings.MUTATION_ENABLED);               
                getGameModel().set(Settings.MUTATION_ENABLED,!enabled);
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
