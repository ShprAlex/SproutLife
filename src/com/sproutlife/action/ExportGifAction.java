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

import com.sproutlife.io.SproutLifeGifWriter;
import com.sproutlife.panel.PanelController;

@SuppressWarnings("serial")
public class ExportGifAction extends AbstractAction {

    protected PanelController controller;
    protected String defaultFileName = "SproutLife";

    private Runnable stopRecordingCallback = null;

    public ExportGifAction(PanelController controller, String name) {
        super(name);
        this.controller = controller;

    }

    public ExportGifAction(PanelController controller) {
        this(controller, "Save Gif");
    }

    public JFileChooser getFileChooser() {
        JFileChooser fileChooser = controller.getFileChooser();
        String date = new SimpleDateFormat(" yyyy-MM-dd").format(new Date());
        if (fileChooser.getSelectedFile() == null
                || !fileChooser.getSelectedFile().getName().toLowerCase().endsWith("gif")) {
            fileChooser.setSelectedFile(new File(defaultFileName + date + ".gif"));
        }
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".gif") || f.isDirectory();
            }

            public String getDescription() {
                return "gif files (*.gif)";
            }
        });
        return fileChooser;
    }

    public void actionPerformed(ActionEvent e) {
        if (stopRecordingCallback != null) {
            stopRecordingCallback.run();
            stopRecordingCallback = null;
            this.putValue(NAME, "Save Gif");
            controller.getGameToolbar().getGifStopRecordingButton().setVisible(false);
            return;
        }

        JFileChooser fileChooser = getFileChooser();
        controller.getScrollController().updateScrollBars();
        controller.setPlayGame(false);

        int returnVal = fileChooser.showSaveDialog(controller.getGameFrame());
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File saveFile = fileChooser.getSelectedFile();
        String fileName = saveFile.getName();
        if (fileName.indexOf(".") < 0) {
            try {
                String filePath = saveFile.getPath();
                saveFile = new File(filePath + ".gif");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        try {
            stopRecordingCallback = SproutLifeGifWriter.saveImage(saveFile, controller);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(controller.getGameFrame(), ex.getMessage());
        }

        this.putValue(NAME, "Stop Recording Gif");
        controller.getGameToolbar().getGifStopRecordingButton().setVisible(true);
        controller.getGameToolbar().getGifStopRecordingButton().setText("GIF Stop Rec.");
    }
}
