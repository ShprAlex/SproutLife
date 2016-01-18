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
