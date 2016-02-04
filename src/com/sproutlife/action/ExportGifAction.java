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

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.sproutlife.io.GifSequenceWriter;
import com.sproutlife.panel.PanelController;

@SuppressWarnings("serial")
public class ExportGifAction extends AbstractAction {
    
    protected PanelController controller;
    protected String defaultFileName = "SproutLife";
    
    private JFileChooser chooser = null;
    
    public ExportGifAction(PanelController controller, String name) {
        super(name);
        this.controller = controller;
        
    }
    
    public ExportGifAction(PanelController controller) {
        this(controller, "Save Gif");
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
        defaultFileName+=date+".gif";
        chooser.setSelectedFile(new File(defaultFileName));
        chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".gif")
                        || f.isDirectory();
            }

            public String getDescription() {
                return "gif files (*.gif)";
            }
        });
    }
    
    public void actionPerformed(ActionEvent e) {
        initChooser();
        controller.getScrollController().updateScrollBars();
        controller.getGameModel().setPlayGame(false);
        //TODO: have play/pause button text update itself 
        controller.getMainControlPanel().getStartPauseButton().setText("Play");
        int returnVal = chooser.showSaveDialog(controller.getGameFrame());
        File saveFile;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            saveFile = chooser.getSelectedFile();
            String fileName = saveFile.getName();
            if (fileName.indexOf(".") < 0) {
                try {
                    String filePath = saveFile.getPath();
                    saveFile = new File(filePath + ".gif");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            final File finalSaveFile = saveFile;
            new Thread() {
                
                @Override
                public void run() {
                    try {
                        saveImage(finalSaveFile);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(controller.getGameFrame(), "Image is too large, or nothing to draw");
                    }
                    
                }
            }.start();
            

        }
    }    
    
    public void saveImage(File saveFile) throws Exception {

        int width = (int) controller.getBoardRenderer().getRendererBounds().getWidth();
        width = Math.min(width,controller.getScrollPanel().getViewportSize().width);
        int height = width*9/16;
        BufferedImage firstImage = controller.getImageManager().getCroppedExportImage(width, height);
        

        // create a new BufferedOutputStream with the last argument
        ImageOutputStream output = 
                new FileImageOutputStream(saveFile);

        // create a gif sequence with the type of the first image, 1 second
        // between frames, which loops continuously
        GifSequenceWriter writer = 
                new GifSequenceWriter(output, firstImage.getType(), 1, true);

        // write out the first image to our sequence...
        writer.writeToSequence(firstImage);
        
        for(int i=0; i<200; i++) {
            int skipFrames = 4;
            if (controller.getBoardRenderer().getBlockSize()==4) {
                skipFrames = 2;
            }
            if (controller.getBoardRenderer().getBlockSize()>4) {
                skipFrames = 1;
            }
            for(int j=0; j<skipFrames; j++) {
                controller.getInteractionLock().writeLock().lock();
                controller.getGameModel().performGameStep();
                controller.getInteractionLock().writeLock().unlock();
                controller.getImageManager().repaintNewImage();
            }
            
            BufferedImage nextImage = controller.getImageManager().getCroppedExportImage(width, height);
            writer.writeToSequence(nextImage);
        }
        
        writer.close();
        output.close();

             
    }
}
