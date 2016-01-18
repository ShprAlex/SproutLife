package com.sproutlife;

import java.util.HashMap;

public class Settings {
    HashMap<String, Object> settings;
    
    public static String MUTATION_ENABLED = "mutationEnabled";
    public static String CHILD_ONE_ENERGY = "childOneEnergy";
    public static String CHILD_TWO_ENERGY = "childTwoEnergy";
    public static String CHILD_THREE_ENERGY = "childThreeEnergy";
    
    public Settings() {
        settings = new HashMap<String, Object>();
        initSettings();
	}
    
    public void initSettings() {
    	set(Settings.MUTATION_ENABLED, true);
    	
        set(Settings.CHILD_ONE_ENERGY, 2);
        set(Settings.CHILD_TWO_ENERGY, 2);
        set(Settings.CHILD_THREE_ENERGY, 2);

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
}
