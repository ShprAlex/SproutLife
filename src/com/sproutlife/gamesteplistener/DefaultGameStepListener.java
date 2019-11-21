package com.sproutlife.gamesteplistener;

import javax.swing.SwingUtilities;

import com.sproutlife.model.step.GameStepEvent;
import com.sproutlife.model.step.GameStepListener;
import com.sproutlife.model.step.GameStep.StepType;
import com.sproutlife.panel.PanelController;

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
                updateStatsPanelText();
            }
        }
    }

    private void updateStatsPanelText() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                pc.getStatsPanel().getStatsTextPane().setText(
                        pc.getGameModel().getStats().getDisplayText());
            }
        });
    }
}
