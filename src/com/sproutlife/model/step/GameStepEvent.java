/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.step;

import com.sproutlife.model.step.GameStep.StepType;

public class GameStepEvent {

    StepType stepType;
    
    public GameStepEvent(StepType stepType) {
        this.stepType = stepType;
    }
    
    public StepType getStepType() {
        return stepType;
    }
}
