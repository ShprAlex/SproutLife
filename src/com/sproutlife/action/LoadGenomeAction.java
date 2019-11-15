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

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.sproutlife.Settings;
import com.sproutlife.io.GenomeIo;
import com.sproutlife.panel.PanelController;

@SuppressWarnings("serial")
public class LoadGenomeAction extends AbstractAction {

    protected PanelController controller;
    private JFileChooser chooser = null;
    static public int colorKind = 0;

    public LoadGenomeAction(PanelController controller, String name) {
        super(name);
        this.controller = controller;
    }

    public LoadGenomeAction(PanelController controller) {
        this(controller, "Load Genome");
    }

    public void initChooser() {
        File cd = null;
        if (chooser!=null) {
            cd = chooser.getCurrentDirectory();
        }
        chooser = new JFileChooser();
        if (cd==null) {
            chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        }
        else {
            chooser.setCurrentDirectory(cd);
        }
        chooser.setAcceptAllFileFilterUsed(false);

        chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".txt")
                        || f.isDirectory();
            }

            public String getDescription() {
                return "txt files (*.txt)";
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        initChooser();
        controller.setPlayGame(false);
        int returnVal = chooser.showOpenDialog(controller.getGameFrame());
        File loadFile;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            loadFile = chooser.getSelectedFile();
            String fileName = loadFile.getName();
            if (fileName.indexOf(".") < 0) {
                try {
                    String filePath = loadFile.getPath();
                    loadFile = new File(filePath + ".txt");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            try {
                GenomeIo.loadGenome(loadFile, controller.getGameModel(), colorKind);
                if (colorKind>=1) {
                    // Kind of a hack for now.
                    // colorKind is set to 0 when a user resets, and incremented after they load.
                    // When colorKind is 1 or more that means the user loaded a second or third genome, so
                    // we switch to the tri-color "splitColor" mode so they can watch them compete
                    controller.getDisplayControlPanel().getChckbxAutoSplitColors().setSelected(false);
                    controller.getSettings().set(Settings.COLOR_MODEL, "SplitColorModel");
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
            colorKind=(colorKind+2)%3;
        }
    } 
}
