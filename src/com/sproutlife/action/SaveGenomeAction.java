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
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.sproutlife.io.GenomeIo;
import com.sproutlife.panel.PanelController;

@SuppressWarnings("serial")
public class SaveGenomeAction extends AbstractAction {
    
    protected PanelController controller;
    
    private JFileChooser chooser = null;
    
    public SaveGenomeAction(PanelController controller, String name) {
        super(name);
        this.controller = controller;
    }
    
    public SaveGenomeAction(PanelController controller) {
        this(controller, "Save Genome");
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
        String date = new SimpleDateFormat(" yyyy-MM-dd HHmm").format(new Date());
        String defaultFileName="Genome"+date+".txt";
        chooser.setSelectedFile(new File(defaultFileName));
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
        int returnVal = chooser.showSaveDialog(controller.getGameFrame());
        File saveFile;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            saveFile = chooser.getSelectedFile();
            String fileName = saveFile.getName();
            if (fileName.indexOf(".") < 0) {
                try {
                    String filePath = saveFile.getPath();
                    saveFile = new File(filePath + ".txt");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            try {
                GenomeIo.saveGenome(saveFile, controller.getGameModel());
            }
            catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(controller.getGameFrame(),
                        "Save Error",
                        "Error saving genome",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
