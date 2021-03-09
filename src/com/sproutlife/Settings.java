/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife;

import java.util.HashMap;

import com.sproutlife.model.seed.SeedFactory.SeedType;

public class Settings {
    HashMap<String, Object> settings;

    public static String SEED_TYPE = "seedType";
    public static String LIFE_MODE = "competitiveMode";
    public static String MUTATION_ENABLED = "mutationEnabled";
    public static String MUTATION_RATE = "mutationRate";
    public static String MAX_LIFESPAN = "maxLifespan";
    public static String TARGET_LIFESPAN = "targetLifespan";
    public static String CHILD_ONE_PARENT_AGE = "minAgeToHaveChildren";
    public static String CHILD_TWO_PARENT_AGE = "childTwoParentAge";
    public static String CHILD_THREE_PARENT_AGE = "childThreeParentAge";
    //Determines whether seeds are sprouted immediately upon detection,
    //Or a step later so seed cells can be displayed before the sprout is.
    public static String SPROUT_DELAYED_MODE = "sproutDelayedMode";
    public static String BACKGROUND_THEME = "backgroundTheme";
    public static String COLOR_MODEL = "colorModel";
    public static String AUTO_SPLIT_COLORS = "autoSplitColors";
    public static String PRIMARY_HUE_DEGREES = "primaryHueDegrees";
    public static String HUE_RANGE = "hueRange";


    public Settings() {
        settings = new HashMap<String, Object>();
        initSettings();
    }

    public void initSettings() {
        set(Settings.SEED_TYPE, SeedType.Bentline1_RPentomino.toString());

        set(Settings.LIFE_MODE, "competitive1");

        set(Settings.MUTATION_ENABLED, true);
        set(Settings.MUTATION_RATE, 5); //0-10

        set(Settings.MAX_LIFESPAN, 75);
        set(Settings.TARGET_LIFESPAN, 37);

        set(Settings.CHILD_ONE_PARENT_AGE, 0);
        set(Settings.CHILD_TWO_PARENT_AGE, 0);
        set(Settings.CHILD_THREE_PARENT_AGE, 0);

        set(Settings.SPROUT_DELAYED_MODE, false);
        set(Settings.AUTO_SPLIT_COLORS, true);

        set(Settings.COLOR_MODEL, "AngleColorModel");
        set(Settings.BACKGROUND_THEME, "black");
        set(Settings.PRIMARY_HUE_DEGREES, 0);
        set(Settings.HUE_RANGE, 100);
    }

    public void set(String s, Object o) {
        Object previous = settings.get(s);

        // for loading settings from file, cast loaded values based on class of default values
        if (previous!=null && o instanceof String) {
            if (previous instanceof Integer) {
                o = Integer.valueOf((String) o);
            }
            if (previous instanceof Boolean) {
                o = Boolean.valueOf((String) o);
            }
        }

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
