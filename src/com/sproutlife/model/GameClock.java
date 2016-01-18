package com.sproutlife.model;

public class GameClock {
    int time;
    
    public GameClock() {
        time = 0;
    }
   
    public int getTime() {
        return time;
    }
      
    void increment() {
        time++;
    }
    
    void reset() {
        this.time = 0;
    }       
}
