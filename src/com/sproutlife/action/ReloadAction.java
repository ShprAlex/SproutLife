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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.sproutlife.io.GenomeIo;
import com.sproutlife.panel.PanelController;

@SuppressWarnings("serial")
public class ReloadAction extends AbstractAction {
    
    protected PanelController controller;
    
    public ReloadAction(PanelController controller, String name) {
        super(name);
        this.controller = controller;
        this.setEnabled(false);
        
    }
    
    public ReloadAction(PanelController controller) {
        this(controller, "Reload");
    }

    public void actionPerformed(ActionEvent e) {
        controller.getInteractionLock().writeLock().lock();

        controller.getGameModel().resetGame();

        if(!controller.getGameModel().isPlaying()) {
            controller.getGameToolbar().getStartPauseButton().getAction().putValue(NAME, "Start");
        }
        try {
            List<File> loadedFiles = controller.getGameController().getLoadedFiles();
            if (loadedFiles.size()>3) {
                loadedFiles = loadedFiles.subList(loadedFiles.size()-3, loadedFiles.size());
            }
            for (File file :loadedFiles) {
                GenomeIo.loadGenome(file, controller.getGameModel());
            }
            controller.updateFromSettings();
            controller.getImageManager().repaintNewImage();
            System.out.println("Loaded Orgs " + controller.getGameModel().getEchosystem().getOrganisms().size());
        }
        catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(controller.getGameFrame(),
                    "Load Error",
                    ex.toString(),
                    JOptionPane.ERROR_MESSAGE);
        }
        controller.getInteractionLock().writeLock().unlock();
    }        
}
