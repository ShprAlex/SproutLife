/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.step;

import com.sproutlife.Settings;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.step.lifemode.CrispLife;
import com.sproutlife.model.step.lifemode.FriendlyLife;
import com.sproutlife.model.step.lifemode.LifeMode;
import com.sproutlife.model.step.lifemode.SimpleLife;
import com.sproutlife.model.step.lifemode.WildLife;

public class LifeStep extends Step {
    LifeMode simpleLife;
    LifeMode friendlyLife;
    LifeMode crispLife;
    LifeMode wildLife;
    LifeMode lifeMode;

    public LifeStep(GameModel gameModel) {
        super(gameModel); 
        simpleLife = new SimpleLife(gameModel);
        friendlyLife = new FriendlyLife(gameModel);
        crispLife = new CrispLife(gameModel);
        wildLife = new WildLife(gameModel);
        
        lifeMode = simpleLife;
    }

    public void perform() {
        initStats();
        updateLifeMode();
        lifeMode.perform();
    }

    public LifeMode getLifeMode() {
        return lifeMode;
    }

    private void updateLifeMode() {
        if("friendly".equals(getSettings().getString(Settings.LIFE_MODE))) {
            this.lifeMode = friendlyLife;
        }
        else if("competitive1".equals(getSettings().getString(Settings.LIFE_MODE))) {
            this.lifeMode = crispLife;
        }
        else if("competitive2".equals(getSettings().getString(Settings.LIFE_MODE))) {
            this.lifeMode = wildLife;
        }
        else {
            this.lifeMode = simpleLife;
        }
    }

    private void initStats() {
        getStats().born = 0;
        getStats().die1 = 0;
        getStats().die2 = 0;
        getStats().stayed = 0;
        getStats().gridSize = getBoard().getWidth()*getBoard().getHeight();    
    }
}
