/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.panel.gamepanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.sproutlife.panel.PanelController;
import com.sproutlife.renderer.RendererUtils;

public class ImageManager {
    public interface PaintImageListener {
        public void paintImage();
    }
    
    public enum LogoStyle {
        None, Small, Large;
    }
    
    private static Color BACK_COLOR = Color.white;
    private static int SAVE_IMAGE_PAD = 0;
    
    public static void setBackgroundColor(Color color) { 
        BACK_COLOR = color; 
    }
    public static void setImagePad(int pad) {
        SAVE_IMAGE_PAD = pad;
    }
    
    private PanelController panelController;
    private LogoStyle logoStyle;
    
    public static final Image applicationLogoImage = null; 
        
    public static Image smallLogoImage = null;
       
    public static Image largeLogoImage = null;
        
    
    private Image drawImage;
    private boolean imageIsDirty;
    private String message = null;
    private boolean enablePaint = true; //Set enable paint to false to prevent updating a dirty image
    
    protected Vector<PaintImageListener> paintImageListeners;
    
    public ImageManager(PanelController panelController, LogoStyle logoStyle) {
        
        this.panelController = panelController;
        this.logoStyle = logoStyle;
        paintImageListeners = new Vector<PaintImageListener>();
        
        try {
            smallLogoImage = new ImageIcon(ImageManager.class.getResource("/images/SproutLife_Logo.png")).getImage();
        }
        catch (Exception ex) {
            //This exception probably means that /resources/ should be added to the classpath
            ex.printStackTrace();
        }
        largeLogoImage = smallLogoImage;
    }
    
    private boolean testAndClearImageDirtyFlag() {
        // Image Dirty Flag must be cleared immediately after testing to avoid
        // potential race conditions when repainting from multiple threads at once.
        boolean imageWasDirty = imageIsDirty && enablePaint;
        if (enablePaint) {
            imageIsDirty = false;
        }
        return imageWasDirty;
    }
    
    public void setEnablePaint(boolean enablePaint) {
        this.enablePaint = enablePaint;
    }
    
    private void flagImageAsDirty() {
        imageIsDirty = true;
    }
    
    protected synchronized void paint(Graphics2D g2) {
        if ( drawImage == null ) {
            createImage();
        }
        if ( testAndClearImageDirtyFlag() ) {
            Image logoImage = getLogoImage();
            paintImage(drawImage, logoImage, panelController.getBoardRenderer().getTransform());
            firePaintImage();
        }
        g2.drawImage(drawImage, 0, 0, null);
    }
    
    protected Image getLogoImage() {
        if ( logoStyle == LogoStyle.Large ) {
            return largeLogoImage;
        }
        else if ( logoStyle == LogoStyle.Small ) {
            return applicationLogoImage;
        }
        
        return null;
    }
    
    public Image getLogoImage(double w, double h) {        
        Image logoImage = smallLogoImage;
        if ( w > 1024 &&
             h > 768 ) {
            logoImage = largeLogoImage;
        }
        return logoImage;
    }
    
    public void repaintNewImage() {
        panelController.getBoardRenderer().updateVisibleRenderers(panelController.getScrollController().getViewportRendererBounds());
        flagImageAsDirty();
        panelController.getScrollPanel().repaint();
    }
    
    protected void paintImage(Image image, Image logoImage, AffineTransform transform) {
        if ( panelController.getBoardRenderer() != null ) {
            paintBackground(image);
             
            Graphics2D g2 = (Graphics2D)image.getGraphics();                        
           
            g2.setTransform(transform);
            panelController.getBoardRenderer().paint(g2);
            paintMessage(image);           
            
            g2.setTransform(new AffineTransform());
            if ( logoImage != null ) {
                // draw the logo in the bottom right corner
                g2.setTransform(new AffineTransform());
                g2.drawImage(logoImage,
                             image.getWidth(null)-logoImage.getWidth(null),
                             image.getHeight(null)-logoImage.getHeight(null),
                             null);
            }
        }
    }

    protected ByteArrayOutputStream paintImagePdf(Image logoImage, int width, int height, AffineTransform transform) {
        try {
            
        Class<?> documentClass = Class.forName("com.lowagie.text.Document");
        Class<?> pageSizeClass = Class.forName("com.lowagie.text.PageSize");
        Class<?> defaultFontMapperClass = Class.forName("com.lowagie.text.pdf.DefaultFontMapper");
        Class<?> fontMapperClass = Class.forName("com.lowagie.text.pdf.FontMapper");
        Class<?> pdfContentByteClass = Class.forName("com.lowagie.text.pdf.PdfContentByte");
        Class<?> pdfTemplateClass = Class.forName("com.lowagie.text.pdf.PdfTemplate");
        Class<?> pdfWriterClass = Class.forName("com.lowagie.text.pdf.PdfWriter"); 
        Class<?> rectangleClass = Class.forName("com.lowagie.text.Rectangle");
        

        /*
            Class<?> documentClass = Class.forName("com.itextpdf.text.Document");
            Class<?> pageSizeClass = Class.forName("com.itextpdf.text.PageSize");
            Class<?> defaultFontMapperClass = Class.forName("com.itextpdf.text.pdf.DefaultFontMapper");
            Class<?> fontMapperClass = Class.forName("com.itextpdf.text.pdf.FontMapper");
            Class<?> pdfContentByteClass = Class.forName("com.itextpdf.text.pdf.PdfContentByte");
            Class<?> pdfTemplateClass = Class.forName("com.itextpdf.text.pdf.PdfTemplate");
            Class<?> pdfWriterClass = Class.forName("com.itextpdf.text.pdf.PdfWriter"); 
            Class<?> rectangleClass = Class.forName("com.itextpdf.text.Rectangle");
*/
        

        if ( panelController.getBoardRenderer() != null ) {
            
            Object doc = documentClass.getConstructor().newInstance();
            documentClass.getMethod("addProducer").invoke(doc);
            documentClass.getMethod("addTitle",String.class).invoke(doc,"Navigator");            
            
            Object rectangle = pageSizeClass.getField("_11X17").get(pageSizeClass);                        
            
            //doc.setPageSize(new com.lowagie.text.Rectangle(PageSize.LETTER.getHeight()*2,PageSize.LETTER.getWidth()*2));            
            documentClass.getMethod("setPageSize", rectangleClass).invoke(doc,rectangleClass.getMethod("rotate").invoke(rectangle));
            
            
            ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
            Object docWriter = null;
            Object dc = null;
            try {
                
                //docWriter = PdfWriter.getInstance((Document) doc, baosPDF);
                docWriter = pdfWriterClass.getMethod("getInstance",documentClass,OutputStream.class).invoke(pdfWriterClass, doc, baosPDF);                

                //doc.open();
                documentClass.getMethod("open").invoke(doc);

                //dc = docWriter.getDirectContent();
                dc = pdfWriterClass.getMethod("getDirectContent").invoke(docWriter);
            }
            catch (Exception ex) { ex.printStackTrace();}
            

            // get a pdf template from the direct content
            //PdfTemplate tp = ((PdfContentByte) dc).createTemplate(width, height);          
            Object tp = pdfContentByteClass.getMethod("createTemplate",float.class, float.class).invoke(dc,width,height);

            // create an AWT renderer from the pdf template
            //Graphics2D g2 = tp.createGraphics(image.getWidth(null), image.getHeight(null), new DefaultFontMapper() );
            Graphics2D g2 = (Graphics2D) pdfTemplateClass.getMethod("createGraphics",float.class,float.class,fontMapperClass).invoke(
                tp,width, height,defaultFontMapperClass.getConstructor().newInstance());
            
            paintBackground(g2,width,height);
            
            //Graphics2D g2 = (Graphics2D)image.getGraphics();                        
            g2.setTransform(transform);
           
            AffineTransform oldTransform = new AffineTransform();
            oldTransform.setTransform(panelController.getBoardRenderer().getTransform());
            
            panelController.getBoardRenderer().getTransform().setToScale(1,1);   
            //panelController.getBoardRenderer().updateLabels();
            panelController.getBoardRenderer().paint(g2);
            panelController.getBoardRenderer().getTransform().setTransform(oldTransform);            
                        
            
            if ( logoImage != null ) {
                // draw the logo in the bottom right corner
                g2.setTransform(new AffineTransform());
                g2.drawImage(logoImage,
                    width-logoImage.getWidth(null),
                    height-logoImage.getHeight(null),
                    null);
            }
            
            //((PdfContentByte) dc).addTemplate(tp, (1224-image.getWidth(null))/2 ,(792-image.getHeight(null))/2);
            pdfContentByteClass.getMethod("addTemplate", pdfTemplateClass,float.class,float.class).invoke(
                dc, tp,(1224-width)/2 ,(792-height)/2);
            g2.dispose();
            try {
                if (doc != null && (Boolean) documentClass.getMethod("isOpen").invoke(doc)) {
                    documentClass.getMethod("close").invoke(doc);
                    //doc.close();
                }
                if (docWriter != null) {
                    pdfWriterClass.getMethod("close").invoke(docWriter);
                    //docWriter.close();
                } 
            }
            catch (Exception ex) { ex.printStackTrace();}
            
            return baosPDF;
        }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public BufferedImage generateScaledImage(int maxImageWidth, int maxImageHeight,
            Image logoImage, int pad) {
        Rectangle2D.Double imageRectangle = panelController.getScrollController().getRendererRectangle();
        Rectangle newImageDimension = RendererUtils.getAspectScaledRectangle(imageRectangle, maxImageWidth, maxImageHeight);
        return generateImage(newImageDimension.width, newImageDimension.height,
        logoImage, pad);
    }
    
    public BufferedImage generateImage(int imageWidth, int imageHeight,
                                       Image logoImage, int pad) {
        try {
            panelController.getInteractionLock().readLock().lock();
            if ( panelController.getScrollController().getVisibleRectangle().width == 0 ||
                 panelController.getScrollController().getVisibleRectangle().height == 0 ) {
                return null;
            }
            BufferedImage image = new BufferedImage(imageWidth, imageHeight,
                BufferedImage.TYPE_INT_RGB);
            int actualImageWidth = imageWidth-2*pad;
            int actualImageHeight = imageHeight-2*pad;
            Rectangle2D.Double imageRectangle = panelController.getScrollController().getRendererRectangle();
            Rectangle newImageDimension = RendererUtils.getAspectScaledRectangle(imageRectangle, actualImageWidth, actualImageHeight);
            double xZoomFactor = (newImageDimension.width / imageRectangle.width);
            double yZoomFactor = (newImageDimension.height / imageRectangle.height);
            int xOffset = 0, yOffset = 0;
            if ( newImageDimension.width < actualImageWidth ) {
                xOffset = (actualImageWidth - newImageDimension.width) / 2;
            }
            if ( newImageDimension.height < actualImageHeight ) {
                yOffset = (actualImageHeight - newImageDimension.height) / 2;
            }
            AffineTransform transform = new AffineTransform();
            transform.translate(pad+xOffset, pad+yOffset);
            transform.scale(xZoomFactor*panelController.getScrollController().getScalingZoomFactor(), yZoomFactor*panelController.getScrollController().getScalingZoomFactor());
            transform.translate(-imageRectangle.getMinX()/panelController.getScrollController().getScalingZoomFactor(),
                -imageRectangle.getMinY()/panelController.getScrollController().getScalingZoomFactor());
            paintImage(image, logoImage, transform);
              
            return image;
        }
        finally {
            panelController.getInteractionLock().readLock().unlock();
        }
    }
    
    public ByteArrayOutputStream generatePdf() {
        Rectangle2D.Double imageRectangle1 = getPaddedExportImageSize();
        Rectangle imageRectangle2  = RendererUtils.getAspectScaledRectangle(imageRectangle1, 1224, 792);
        
        Image logoImage = smallLogoImage;
        int imageWidth = imageRectangle2.width;
        int imageHeight = imageRectangle2.height;
       
        
        int pad = SAVE_IMAGE_PAD;
        
        try {
            panelController.getInteractionLock().readLock().lock();
            if ( panelController.getScrollController().getVisibleRectangle().width == 0 ||
                    panelController.getScrollController().getVisibleRectangle().height == 0 ) {
                return null;
            }
            //BufferedImage image = new BufferedImage(imageWidth, imageHeight,
            //    BufferedImage.TYPE_INT_RGB);
            int actualImageWidth = imageWidth-2*pad;
            int actualImageHeight = imageHeight-2*pad;
            Rectangle2D.Double imageRectangle = panelController.getScrollController().getRendererRectangle();
            Rectangle newImageDimension = RendererUtils.getAspectScaledRectangle(imageRectangle, actualImageWidth, actualImageHeight);
            double xZoomFactor = (newImageDimension.width / imageRectangle.width);
            double yZoomFactor = (newImageDimension.height / imageRectangle.height);
            int xOffset = 0, yOffset = 0;
            if ( newImageDimension.width < actualImageWidth ) {
                xOffset = (actualImageWidth - newImageDimension.width) / 2;
            }
            if ( newImageDimension.height < actualImageHeight ) {
                yOffset = (actualImageHeight - newImageDimension.height) / 2;
            }
            AffineTransform transform = new AffineTransform();
            transform.translate(pad+xOffset, pad+yOffset);
            transform.scale(xZoomFactor*panelController.getScrollController().getScalingZoomFactor(), yZoomFactor*panelController.getScrollController().getScalingZoomFactor());
            transform.translate(-imageRectangle.getMinX()/panelController.getScrollController().getScalingZoomFactor(),
                -imageRectangle.getMinY()/panelController.getScrollController().getScalingZoomFactor());
            return paintImagePdf(logoImage, imageWidth, imageHeight, transform);
            
        }
        finally {
            panelController.getInteractionLock().readLock().unlock();
        }                       
    }    
    
    public Rectangle2D.Double getRendererBounds(int imageWidth, int imageHeight,
                                                Point2D.Double centerLocation) {
        double actualWidth = imageWidth / panelController.getBoardRenderer().getZoom();
        double actualHeight = imageHeight / panelController.getBoardRenderer().getZoom();
        return new Rectangle2D.Double(centerLocation.x - actualWidth/2,
                                      centerLocation.y - actualHeight/2,
                                      actualWidth, actualHeight);
    }
    
    public BufferedImage generateCenteredImage(int imageWidth, int imageHeight,
                                               Point2D.Double centerLocation,
                                               Image logoImage) {
        try {
            panelController.getInteractionLock().readLock().lock();
            if ( panelController.getScrollController().getVisibleRectangle().width == 0 ||
                 panelController.getScrollController().getVisibleRectangle().height == 0 ) {
                return null;
            }
            BufferedImage image = new BufferedImage(imageWidth, imageHeight,
                BufferedImage.TYPE_INT_RGB);
            
            Rectangle2D.Double bounds = getRendererBounds(imageWidth, imageHeight, centerLocation);
            AffineTransform transform = new AffineTransform();
            transform.scale(panelController.getScrollController().getScalingZoomFactor(), panelController.getScrollController().getScalingZoomFactor());
            transform.translate(-bounds.getMinX(),-bounds.getMinY());
 
            paintImage(image, logoImage, transform);
            return image;
        }
        finally {
            panelController.getInteractionLock().readLock().unlock();
        }
    }
    
    public BufferedImage generateCenteredImage(Rectangle2D.Double bounds) {
        Image logoImage = getLogoImage(bounds.height, bounds.width);
        return generateCenteredImage(bounds, logoImage); 
    }
    
    public BufferedImage generateCenteredImage(Rectangle2D.Double bounds, Image logoImage) {        
        
        try {
            panelController.getInteractionLock().readLock().lock();
            if ( panelController.getScrollController().getVisibleRectangle().width == 0 ||
                    panelController.getScrollController().getVisibleRectangle().height == 0 ) {
                return null;
            }
            double scalingZoomFactor = panelController.getScrollController().getScalingZoomFactor();
            BufferedImage image = new BufferedImage(
                (int)Math.ceil(bounds.width * scalingZoomFactor),
                (int)Math.ceil(bounds.height * scalingZoomFactor),
                BufferedImage.TYPE_INT_RGB);

            AffineTransform transform = new AffineTransform();
            transform.scale(scalingZoomFactor, scalingZoomFactor);
            transform.translate(-bounds.getMinX(),-bounds.getMinY());
            paintImage(image, logoImage, transform);
            return image;
        }
        finally {
            panelController.getInteractionLock().readLock().unlock();
        }
    }
    
    public Rectangle2D.Double getPaddedExportImageSize() {
        return panelController.getScrollController().getRendererRectangle(SAVE_IMAGE_PAD);
    }
    
    public BufferedImage getExportImage() {
        Rectangle2D.Double imageRectangle = getPaddedExportImageSize();
        
        Image logoImage = getLogoImage(imageRectangle.width, imageRectangle.height);

        BufferedImage image = generateImage((int)imageRectangle.width,
                                            (int)imageRectangle.height,
                                            logoImage,
                                            SAVE_IMAGE_PAD);
        
        return image;
    }
    
    public BufferedImage getScaledExportImage(int imageWidth, int imageHeight) {
        
        Image logoImage = getLogoImage(imageWidth, imageHeight);

        BufferedImage scaledImage = generateImage(imageWidth,imageHeight,
                                            logoImage,
                                            SAVE_IMAGE_PAD);      
        return scaledImage;
    }
    
    public BufferedImage getCroppedExportImage(int imageWidth, int imageHeight) {
        Image logoImage = getLogoImage(imageWidth, imageHeight);
        
        BufferedImage image = generateCenteredImage(imageWidth, imageHeight,
            panelController.getScrollController().getCenterDrawPosition(), logoImage);
        
        return image;
    }        
    
    public void saveImage(File saveFile) throws Exception {
        BufferedImage image = getExportImage();

        if( image != null ) {
            try {
                // save the drawing to a PNG file
                ImageIO.write(image, "png", saveFile);
                System.gc();
            }
            catch (Exception ex) { ex.printStackTrace(); }
        }               
    }
    
    public void saveCroppedImage(File saveFile, int imageWidth, int imageHeight) throws Exception {
        BufferedImage image = getCroppedExportImage(imageWidth, imageHeight);
        
        if( image != null ) {
            try {
                // save the drawing to a PNG file
                ImageIO.write(image, "png", saveFile);
            }
            catch (Exception ex) { ex.printStackTrace(); }
        }
    }
    
    public void saveScaledImage(File saveFile, int imageWidth, int imageHeight) throws Exception {

        BufferedImage scaledImage = getScaledExportImage(imageWidth, imageHeight);
        
        if( scaledImage != null ) {
            try {
                // save the drawing to a PNG file
                ImageIO.write(scaledImage, "png", saveFile);
                System.gc();
            }
            catch (Exception ex) { ex.printStackTrace(); }
        }               
    }
    
    protected synchronized void createImage() {
        drawImage = new BufferedImage(panelController.getScrollController().getVisibleRectangle().width,
                                      panelController.getScrollController().getVisibleRectangle().height,
                                      BufferedImage.TYPE_INT_ARGB);
        //Does this need to be here?
        paintBackground(drawImage);
    }
    
    protected void paintBackground(Image image) {
        paintBackground((Graphics2D)image.getGraphics(),image.getWidth(null),image.getHeight(null));        
    }
    
    protected void paintBackground(Graphics2D g2, int width, int height) {      
        g2.setColor(BACK_COLOR);
        g2.fillRect(0,0,width,height);        
    }
    
    public void addPaintImageListener(PaintImageListener paintImageListener) {
        paintImageListeners.add(paintImageListener);
    }
    
    public void removePaintImageListener(PaintImageListener paintImageListener) {
        paintImageListeners.remove(paintImageListener);
    }
    
    private void firePaintImage() {
        for ( PaintImageListener paintImageListener : paintImageListeners ) {
            paintImageListener.paintImage();
        }
    }
    
    public void paintMessage(Image image) {
        if (getMessage()==null) return;
        Graphics2D g2 = (Graphics2D)image.getGraphics();
        g2.setColor(new Color(255,0,0,100));        
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
             RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHints(rh);
                
        g2.setFont(new Font("Arial",Font.PLAIN,40));
        int len = getMessage().length()/2;
        g2.drawString(getMessage(),image.getWidth(null)/2-len*18,image.getHeight(null)/2-20);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
