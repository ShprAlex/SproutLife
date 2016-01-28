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
public class ExportPngAction extends AbstractAction {
    
    protected PanelController controller;
    protected String defaultFileName = "SproutLife";
    
    private JFileChooser chooser = null;
    
    public ExportPngAction(PanelController controller, String name) {
        super(name);
        this.controller = controller;
        
    }
    
    public ExportPngAction(PanelController controller) {
        this(controller, "Save Png");
    }
    
    public void setDefaultFileName(String defaultFileName) {
        this.defaultFileName=defaultFileName;
        if (chooser!=null) {
            chooser.setSelectedFile(new File(defaultFileName));
        }
    }
    
    public void initChooser() {
        if (chooser!=null) return;
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        chooser.setAcceptAllFileFilterUsed(false);
        String date = new SimpleDateFormat(" yyyy-MM-dd").format(new Date());
        defaultFileName+=date+".png";
        chooser.setSelectedFile(new File(defaultFileName));
        chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".png")
                        || f.isDirectory();
            }

            public String getDescription() {
                return "png files (*.png)";
            }
        });
    }
    
    public void actionPerformed(ActionEvent e) {
        initChooser();

        int returnVal = chooser.showSaveDialog(controller.getGameFrame());
        File saveFile;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            saveFile = chooser.getSelectedFile();
            String fileName = saveFile.getName();
            if (fileName.indexOf(".") < 0) {
                try {
                    String filePath = saveFile.getPath();
                    saveFile = new File(filePath + ".png");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            try {
                controller.getImageManager().saveImage(saveFile);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(controller.getGameFrame(), "Image is too large, or nothing to draw");
            }
        }
    }        
}
