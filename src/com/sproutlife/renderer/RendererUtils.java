/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.renderer;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import com.sproutlife.model.echosystem.Organism;

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
    
    /*
     * Once an organism is born we don't draw it at that location right away but instead we
     * smoothly animate it from it's parent birth location it it's birth location.
     *
     * @return a Point2Double location scaled between the organism's location and it's parent location
     *
     */
    public static Point2Double getScaleTowardsBirthLocation(Organism o) {
        Organism parent = o.getParent();
        if (parent==null || o.getChildren().size()>0) {
            //return 1;
        }
        int paab = o.getAttributes().parentAgeAtBirth;
        if (parent.getParent()!=null) {
            // min of parent and grandparent age of having child.
            paab = (paab + parent.getAttributes().parentAgeAtBirth)/2;
        }
        double scale = Math.min(o.getAge(),paab)/(double) paab;
        double x = parent.x+((o.x-parent.x)*scale);
        double y = parent.y+((o.y-parent.y)*scale);
        return new Point2Double(x,y);
    }

}