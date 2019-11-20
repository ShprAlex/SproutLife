/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.sproutlife.panel.PanelController;

@SuppressWarnings("serial")
public class PlayGameAction extends AbstractAction {
    
    protected PanelController controller;
    
    public PlayGameAction(PanelController controller, String name) {
        super(name);
        this.controller = controller;
        
    }
    
    public PlayGameAction(PanelController controller) {
        this(controller, "Start");
    }
    
        
    public void actionPerformed(ActionEvent e) {
        setPlayGame(!controller.getGameModel().isPlaying());
    } 
    
    public void setPlayGame(boolean playGame) {
        if (playGame==false) {
            controller.getGameModel().setPlayGame(false);
            this.putValue(NAME, "Play");  
            //controller.getMainControlPanel().getStartPauseButton().setText("Play");
        }
        else {
            controller.getGameModel().setPlayGame(true);
            //controller.getMainControlPanel().getStartPauseButton().setText("Pause");
            this.putValue(NAME, "Pause");
        }
    }
}
