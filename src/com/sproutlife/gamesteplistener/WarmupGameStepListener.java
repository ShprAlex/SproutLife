package com.sproutlife.gamesteplistener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sproutlife.model.GameModel;
import com.sproutlife.model.GameThread;
import com.sproutlife.model.step.GameStep.StepType;
import com.sproutlife.panel.PanelController;
import com.sproutlife.model.step.GameStepEvent;
import com.sproutlife.model.step.GameStepListener;

public class WarmupGameStepListener implements GameStepListener{
    PanelController pc;
    boolean autoAdjustSpeed;
    boolean autoAdjustDisplayLayers;

    public WarmupGameStepListener(PanelController panelController) {
        this.pc = panelController;
        this.autoAdjustSpeed = true;
        this.autoAdjustDisplayLayers = true;
        addPanelListeners();
    }

    public boolean isAutoAdjustSpeed() {
        return autoAdjustSpeed;
    }

    public void setAutoAdjustSpeed(boolean autoAdjustSpeed) {
        this.autoAdjustSpeed = autoAdjustSpeed;
    }

    public boolean isAutoAdjustDisplayLayers() {
        return autoAdjustDisplayLayers;
    }

    public void setAutoAdjustDisplayLayers(boolean autoAdjustDisplayLayers) {
        this.autoAdjustDisplayLayers = autoAdjustDisplayLayers;
    }

    private GameModel getGameModel() {
        return pc.getGameModel();
    }

    private GameThread getGameThread() {
        return getGameModel().getGameThread();
    }

    @Override
    public void stepPerformed(GameStepEvent event) {
        if (event.getStepType() == StepType.STEP_BUNDLE) {
            if (isAutoAdjustSpeed()) {
                updateGameThread();
            }
            if (pc.getGameModel().getTime()%100==0) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        doWarmup();
                    }
                });
            }
        }
    }

    private void doWarmup() {
        if (isAutoAdjustSpeed() && pc.getGameModel().getTime()>5000) {
            switch (getIterationsPerEvent()) {
                case 1: pc.getGameToolbar().getSpeedSlider().setValue(0); break;
                case 2: pc.getGameToolbar().getSpeedSlider().setValue(1); break;
                case 4: pc.getGameToolbar().getSpeedSlider().setValue(2); break;
                case 8: pc.getGameToolbar().getSpeedSlider().setValue(4); break;
            };
            setAutoAdjustSpeed(false);
        }
        if (isAutoAdjustDisplayLayers() && pc.getGameModel().getTime()>=6000) {
            pc.getDisplayControlPanel().getChckbxCellLayer().setSelected(false);
            pc.getDisplayControlPanel().getChckbxGenomeLayer().setSelected(false);
            setAutoAdjustDisplayLayers(false);
        }
    }

    private void updateGameThread() {
        getGameThread().setSleepDelay(getSleepDelay());
        getGameThread().setIterationsPerEvent(getIterationsPerEvent());
    }

    private int getSleepDelay() {
        int sleep = 1;

        if (pc.getGameModel().getTime()<100 ) {
            sleep = 800 - (int) (Math.log10(getGameModel().getTime()/13.0+1)*800) ;
        }
        else {
            sleep = Math.max(1, 40-(int) Math.sqrt(getGameModel().getTime()/4));
        }

        if (getGameModel().getTime()<2000 ) {
            sleep = 10;
        }
        else if (getGameModel().getTime()<4000 ) {
            sleep = 8;
        }
        return sleep;
    }

    public int getIterationsPerEvent() {
        int autoIterations = 1;

        if (getGameModel().getEchosystem().getOrganisms().size() > 120) {
            autoIterations = 2;
        }
        if (getGameModel().getEchosystem().getOrganisms().size() > 180) {
            autoIterations = 4;
        }
        if (getGameModel().getEchosystem().getOrganisms().size() > 240) {
            autoIterations = 8;
        }
        return autoIterations;
    }

    private void addPanelListeners() {
        pc.getGameToolbar().getSpeedSlider().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                setAutoAdjustSpeed(false);
            }
        });
        ItemListener layerListener = new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                setAutoAdjustDisplayLayers(false);
            }
        };
        pc.getDisplayControlPanel().getChckbxCellLayer().addItemListener(layerListener);
        pc.getDisplayControlPanel().getChckbxGenomeLayer().addItemListener(layerListener);
        pc.getDisplayControlPanel().getChckbxOrgHeadLayer().addItemListener(layerListener);
        pc.getDisplayControlPanel().getChckbxOrgTailLayer().addItemListener(layerListener);
        pc.getDisplayControlPanel().getChckbxOutlineSeeds().addItemListener(layerListener);
    }
}
