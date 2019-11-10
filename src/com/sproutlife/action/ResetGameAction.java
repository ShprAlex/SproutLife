/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.sproutlife.panel.PanelController;

@SuppressWarnings("serial")
public class ResetGameAction extends AbstractAction {
    
    protected PanelController controller;
    
    public ResetGameAction(PanelController controller, String name) {
        super(name);
        this.controller = controller;
        
    }
    
    public ResetGameAction(PanelController controller) {
        this(controller, "Reset");
    }
    
        
    public void actionPerformed(ActionEvent e) {
        controller.getInteractionLock().writeLock().lock();

        controller.getGameModel().resetGame();

        if(!controller.getGameModel().getPlayGame()) {
            controller.getGameToolbar().getStartPauseButton().getAction().putValue(NAME, "Start");
        }
        controller.getInteractionLock().writeLock().unlock();
        controller.getImageManager().repaintNewImage();      
        LoadGenomeAction.colorKind = 0;
    }        
}
