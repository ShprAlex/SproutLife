package com.sproutlife;

import com.sproutlife.model.GameModel;
import com.sproutlife.model.GameThread;
import com.sproutlife.panel.GameFrame;

public class GameController {
    GameFrame frame;

    private GameModel gameModel;        

    private Settings settings;


    public GameController() {
        settings = new Settings();

        gameModel = new GameModel(settings);        

        frame = new GameFrame(this);

        //game = new GameThread(frame.getGamePanel());
    }

    public void start() {
        frame.setVisible(true); 
        //gameModel.getEchosystem().resetCells();
        frame.getGamePanel().addHandlers();
        frame.getGamePanel().updateBoardSizeFromPanelSize();
    }        

    public GameModel getGameModel() {
        return gameModel;
    }

    public GameFrame getFrame() {
        return frame;
    }

    public Settings getSettings() {
        return settings;
    }

    public void set(String s, Object o) {
        getSettings().set(s,o);
    }    

}
