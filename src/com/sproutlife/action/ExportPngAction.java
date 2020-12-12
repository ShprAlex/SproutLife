/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.action;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
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

    public ExportPngAction(PanelController controller, String name) {
        super(name);
        this.controller = controller;

    }

    public ExportPngAction(PanelController controller) {
        this(controller, "Save Png");
    }

    public JFileChooser getFileChooser() {
        JFileChooser fileChooser = controller.getFileChooser();
        String date = new SimpleDateFormat(" yyyy-MM-dd").format(new Date());
        if (fileChooser.getSelectedFile() == null
                || !fileChooser.getSelectedFile().getName().toLowerCase().endsWith("png")) {
            fileChooser.setSelectedFile(new File(defaultFileName + date + ".png"));
        }
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".png") || f.isDirectory();
            }

            public String getDescription() {
                return "png files (*.png)";
            }
        });
        return fileChooser;
    }

    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = getFileChooser();
        controller.getScrollController().updateScrollBars();
        int returnVal = fileChooser.showSaveDialog(controller.getGameFrame());
        File saveFile;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            saveFile = fileChooser.getSelectedFile();
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
                double zoom = controller.getBoardRenderer().getZoom();
                int width = (int) (controller.getBoardRenderer().getRendererBounds().getWidth() * zoom);
                width = Math.min(width, controller.getScrollPanel().getViewportSize().width);
                int height = (int) (controller.getBoardRenderer().getRendererBounds().getHeight() * zoom);
                height = Math.min(height, controller.getScrollPanel().getViewportSize().height);
                controller.getImageManager().saveCroppedImage(saveFile, width, height);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(controller.getGameFrame(), "Image is too large, or nothing to draw");
            }
        }
    }
}
