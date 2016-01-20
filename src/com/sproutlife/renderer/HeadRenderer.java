package com.sproutlife.renderer;

import java.awt.Color;
import java.awt.Graphics2D;

import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Organism;

public class HeadRenderer extends Renderer {

    public HeadRenderer(GameModel gameModel) {
        super(gameModel);		
    }

    public void paintHead(Graphics2D g, Organism o) {
        g.setColor(getColor(o));
        /*
                Point op = new Point(o.getPosition());
                if(o.getParent()!=null) {
                    int age = getGameModel().getAge(o);
                    if (age<10) {
                         op.x=o.getParent().x+(op.x-o.getParent().x)*age/10;
                         op.y=o.getParent().y+(op.y-o.getParent().y)*age/10;
                    }
                }
         */
        if (o.getCells().size()>0) {
            int rectSize = BLOCK_SIZE*3;//(int) Math.sqrt(o.getCells().size()*BLOCK_SIZE*BLOCK_SIZE);
            //g.fillOval(BLOCK_SIZE + (BLOCK_SIZE*o.x)-rectSize/2, BLOCK_SIZE + (BLOCK_SIZE*o.y)-rectSize/2, rectSize, rectSize);
            g.fillRect(BLOCK_SIZE + (BLOCK_SIZE*(o.x-1)), BLOCK_SIZE + (BLOCK_SIZE*(o.y-1)), rectSize, rectSize);
        }

    }

    private Color getColor(Organism o) {
        switch (o.getKind()) {
            case 0: return new Color(255, 176, 176);
            case 1: return new Color(176, 255, 176);
            case 2: return new Color(176, 176 ,255);
        }
        /*
        switch (o.getKind()) {
            case 0: return new Color(255,0,0, 80);
            case 1: return new Color(0,255,0, 80);
            case 2: return new Color(0,60,255, 80);
        }
        */
        return null; 
    }
}
