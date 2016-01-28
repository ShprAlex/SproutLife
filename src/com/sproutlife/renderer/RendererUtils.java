/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.renderer;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class RendererUtils {
    
    public static Rectangle getAspectScaledRectangle(Rectangle2D.Double originalRectangle, int desiredWidth, int desiredHeight) {
        if ( originalRectangle.width == desiredWidth &&
             originalRectangle.height == desiredHeight ) {
            return new Rectangle(0, 0, desiredWidth, desiredHeight);
        }
        double scaleFactor;
        int newHeight;
        int newWidth;
        if ( originalRectangle.width > originalRectangle.height ) {
            scaleFactor = desiredWidth / originalRectangle.width;
            newHeight = (int)Math.floor(scaleFactor * originalRectangle.height);
            if ( newHeight > desiredHeight ) {
                scaleFactor = desiredHeight / originalRectangle.height;
                newWidth = (int)Math.floor(scaleFactor * originalRectangle.width);
                newHeight = desiredHeight;
            }
            else {
                newWidth = desiredWidth;
            }
        }
        else {
            scaleFactor = desiredHeight / originalRectangle.height;
            newWidth = (int)Math.floor(scaleFactor * originalRectangle.width);
            if ( newWidth > desiredWidth ) {
                scaleFactor = desiredWidth / originalRectangle.width;
                newWidth = desiredWidth;
                newHeight = (int)Math.floor(scaleFactor * originalRectangle.height);
            }
            else {
                newHeight = desiredHeight;
            }
        }
        int imgXOffset = 0, imgYOffset = 0;
        if ( newWidth < desiredWidth ) {
            imgXOffset = (int)Math.floor((desiredWidth - newWidth) / 2.0);
        }
        else {
            imgXOffset = 0;
        }
        if ( newHeight < desiredHeight ) {
            imgYOffset = (int)Math.floor((desiredHeight - newHeight) / 2.0);
        }
        else {
            imgYOffset = 0;
        }
        Rectangle previewImageRectangle = new Rectangle(imgXOffset, imgYOffset, newWidth, newHeight);
        return previewImageRectangle;
    }
    
}