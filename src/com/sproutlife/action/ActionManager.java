/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.action;

import com.sproutlife.panel.PanelController;

/**
 * @author Alex Shapiro
 *
 */
public class ActionManager {
    private PanelController controller;
    
    private PlayGameAction playGameAction;
    private ResetGameAction resetGameAction;
    private ExportGifAction exportGifAction;

    public ActionManager(PanelController controller) {
        this.controller = controller;
    }

    /**
     * @return the playGameAction
     */
    public PlayGameAction getPlayGameAction() {
        if (playGameAction == null) {
            playGameAction = new PlayGameAction(controller);
        }
        return playGameAction;
    }

    /**
     * @return the resetGameAction
     */
    public ResetGameAction getResetGameAction() {
        if (resetGameAction == null) {
            resetGameAction = new ResetGameAction(controller);
        }        
        return resetGameAction;
    }

    public ExportGifAction getExportGifAction() {
        if (exportGifAction == null) {
            exportGifAction = new ExportGifAction(controller);
        }
        return exportGifAction;
    }

}
