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
    private ReloadAction reloadAction;
    private ExportGifAction exportGifAction;
    private LoadGenomeAction loadGenomeAction;

    public ActionManager(PanelController controller) {
        this.controller = controller;
    }

    public PlayGameAction getPlayGameAction() {
        if (playGameAction == null) {
            playGameAction = new PlayGameAction(controller);
        }
        return playGameAction;
    }

    public ResetGameAction getResetGameAction() {
        if (resetGameAction == null) {
            resetGameAction = new ResetGameAction(controller);
        }
        return resetGameAction;
    }

    public ReloadAction getReloadAction() {
        if (reloadAction == null) {
            reloadAction = new ReloadAction(controller);
        }
        return reloadAction;
    }

    public ExportGifAction getExportGifAction() {
        if (exportGifAction == null) {
            exportGifAction = new ExportGifAction(controller);
        }
        return exportGifAction;
    }

    public LoadGenomeAction getLoadGenomeAction() {
        if (loadGenomeAction == null) {
            loadGenomeAction = new LoadGenomeAction(controller);
        }
        return loadGenomeAction;
    }
}
