package com.fitzysoft.spaceshooter;

import javafx.scene.Node;
import javafx.scene.effect.DisplacementMap;
import javafx.scene.effect.FloatMap;

/**
 * Created by jamesfitzgerald on 4/16/16.
 */
public class SuperFitzySpriteEffects {


    // todo: needs work -  looks nothing like an explosion!
    //
    public static void explodeEffect1(Node node, int step) {
        // displacement map works like this
        //  dst[x, y] = src[x + (offsetX + scaleX * map[x, y][0]) * srcWidth,
        //          y + (offsetY + scaleY * map[x, y][1]) * srcHeight]


        FloatMap floatMap = new FloatMap();
        int width = (int) node.getBoundsInLocal().getWidth();
        int height = (int) node.getBoundsInLocal().getHeight();
        floatMap.setWidth(width);
        floatMap.setHeight(height);
        for (int i = 0; i < width; i++) {
            //double v = (Math.sin(i / 20.0 * Math.PI) - 0.5) / 40.0;
            double x = Math.random() - 0.5f;
            for (int j = 0; j < height; j++) {
                double y = Math.random() - 0.5f;
                floatMap.setSamples(i, j, (float) x , (float) y);
            }
        }
        DisplacementMap displacementMap = new DisplacementMap();
        displacementMap.setMapData(floatMap);
//        displacementMap.setScaleX(1 + fadeOutFrameCount/100);
//        displacementMap.setScaleY(1 + fadeOutFrameCount/100);
        node.setEffect(displacementMap);
        node.setScaleX(1 + step /10);
        node.setScaleY(1 + step/10);
    }
}
