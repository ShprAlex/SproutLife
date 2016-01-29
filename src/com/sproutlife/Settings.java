/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife;

import java.util.HashMap;

public class Settings {
    HashMap<String, Object> settings;
    public static String SEED_TYPE = "seedType";
    public static String LIFE_MODE = "lifeMode";
    public static String MUTATION_ENABLED = "mutationEnabled";
    public static String MAX_LIFESPAN = "maxLifespan";
    public static String CHILD_ONE_ENERGY = "childOneEnergy";
    public static String CHILD_TWO_ENERGY = "childTwoEnergy";
    public static String CHILD_THREE_ENERGY = "childThreeEnergy";
    
    
    
    public Settings() {
        settings = new HashMap<String, Object>();
        initSettings();
	}
    
    public void initSettings() {
        set(Settings.LIFE_MODE, "friendly");
        
    	set(Settings.MUTATION_ENABLED, true);
    	
    	set(Settings.MAX_LIFESPAN, 100);
    	
        set(Settings.CHILD_ONE_ENERGY, 0);
        set(Settings.CHILD_TWO_ENERGY, 0);
        set(Settings.CHILD_THREE_ENERGY, 0);

    }
    
    public void set(String s, Object o) {
    	settings.put(s, o);
    }
    
    public boolean getBoolean(String s) {
    	return (Boolean) settings.get(s);
    }

    public int getInt(String s) {
    	return (Integer) settings.get(s);
    }
    
    public String getString(String s) {
        return (String) settings.get(s);
    }
}
