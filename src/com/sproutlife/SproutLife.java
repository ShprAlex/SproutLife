/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife;

public class SproutLife {
    private static final int    appMajorVersion    = 0;
    private static final int    appMinorVersion    = 12;
    private static final int    appRevision        = 1;

    public static int getAppMajorVersion() {
        return appMajorVersion;
    }

    public static int getAppMinorVersion() {
        return appMinorVersion;
    }

    public static int getAppRevision() {
        return appRevision;
    }
    
    public static String getAppVersion() {
        return ""+getAppMajorVersion()+"."+getAppMinorVersion()+"."+getAppRevision();
    }

    public static void main(String[] args) {
        new GameController();
    }
}
