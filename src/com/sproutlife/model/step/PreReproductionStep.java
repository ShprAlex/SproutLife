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
import com.sproutlife.model.seed.SeedFactory.SeedType;

/*
 * PreReproductionStep at the beginning of the game, or after we reset.
 * We create random seeds, and cycle the lifespan until reproduction is "discovered".
 */
public class PreReproductionStep extends Step {
    GameModel gameModel;   
       
    public PreReproductionStep(GameModel gameModel) {
        super(gameModel);
    }
        
    public void perform() {
        if(getEchosystem().getOrganisms().size()<40) {
            if (getTime()%200==0) {
                int lifespan = Math.max(16,getEchosystem().getDefaultOrgLifespan());                          
                if (lifespan>getSettings().getInt(Settings.MAX_LIFESPAN)) {
                    lifespan = 15;
                }
                getEchosystem().setDefaultOrgLifespan(lifespan+1);
            }            
        }

        if (getEchosystem().getOrganisms().size()<12) {
            SeedType seedType = SeedType.get(getSettings().getString(Settings.SEED_TYPE));
            SproutStep.sproutRandomSeed(seedType, getEchosystem(), null);
        }
    }
}
