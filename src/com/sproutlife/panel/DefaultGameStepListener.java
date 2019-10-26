package com.sproutlife.panel;

import javax.swing.SwingUtilities;

import com.sproutlife.model.step.GameStepEvent;
import com.sproutlife.model.step.GameStepListener;
import com.sproutlife.model.step.GameStep.StepType;

public class DefaultGameStepListener implements GameStepListener{
    PanelController pc;

    public DefaultGameStepListener(PanelController panelController) {
        this.pc = panelController;
    }

    @Override
    public void stepPerformed(GameStepEvent event) {
        if (event.getStepType() == StepType.STEP_BUNDLE) {
            pc.getImageManager().repaintNewImage();
            
            if (pc.getGameModel().getTime()%100==0) {
                SwingUtilities.invokeLater(new Runnable() {                            
                    @Override
                    public void run() {                                
                        pc.getStatsPanel().getStatsTextPane().setText(
                                pc.getGameModel().getStats().getDisplayText());
                        handleUpdateAfterIntro();
                    }
                });
            }
        }
    }

    private void handleUpdateAfterIntro() {
        // when the user presses the start button we do some things on a "timer" to
        // automatically update certain settings.

        if (pc.getGameModel().getGameThread().getIterations()>=2 &&
                pc.getGameModel().getGameThread().getAutoAdjust() &&
                pc.getGameModel().getTime()>5000) {
            pc.getGameToolbar().getSpeedSlider().setValue(1);
            pc.getGameModel().getGameThread().setAutoAdjust(false);
        }
        if (pc.getGameModel().getTime()>=5000 && pc.getGameModel().getTime()<=5200) {
            pc.getDisplayControlPanel().getChckbxCellLayer().setSelected(false);
            pc.getDisplayControlPanel().getChckbxGenomeLayer().setSelected(false);
        }
    }
}
