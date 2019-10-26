package com.sproutlife.io;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import com.sproutlife.Settings;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Genome;
import com.sproutlife.model.echosystem.Mutation;
import com.sproutlife.model.echosystem.Organism;
import com.sproutlife.model.seed.SeedFactory.SeedType;
import com.sproutlife.model.step.GameStepEvent;
import com.sproutlife.model.step.GameStepListener;
import com.sproutlife.model.step.SproutStep;
import com.sproutlife.panel.PanelController;
import com.sproutlife.model.step.GameStep.StepType;

public class SproutLifeGifWriter {
    public static Runnable saveImage(File saveFile, PanelController controller) throws IOException {
        int width = (int) controller.getBoardRenderer().getRendererBounds().getWidth();
        width = Math.min(width,controller.getScrollPanel().getViewportSize().width);
        int height = (int) controller.getBoardRenderer().getRendererBounds().getWidth();
        height = Math.min(width,controller.getScrollPanel().getViewportSize().height);
        BufferedImage firstImage = controller.getImageManager().getCroppedExportImage(width, height);

        // create a new BufferedOutputStream with the last argument
        ImageOutputStream output = new FileImageOutputStream(saveFile);

        // create a gif sequence with the type of the first image, 1 second
        // between frames, which loops continuously
        GifSequenceWriter writer = new GifSequenceWriter(output, firstImage.getType(), 1, true);

        // write out the first image to our sequence...
        writer.writeToSequence(firstImage);

        final int fwidth = width;
        final int fheight = height;
        GameStepListener gifExportGsl = new GameStepListener() {
            @Override
            public void stepPerformed(GameStepEvent event) {
                if (event.getStepType() == StepType.STEP_BUNDLE) {
                    BufferedImage nextImage = controller.getImageManager().getCroppedExportImage(fwidth, fheight);
                    try {
                        writer.writeToSequence(nextImage);
                    }
                    catch(IOException ex) {}
                }
            }
        };
        controller.getGameModel().getGameThread().addGameStepListener(gifExportGsl);

        // finish saving gif callback
        return new Runnable() {
            @Override
            public void run() {
                controller.getGameModel().getGameThread().removeGameStepListener(gifExportGsl);
                try {
                    writer.close();
                    output.close();
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };
    }
}
