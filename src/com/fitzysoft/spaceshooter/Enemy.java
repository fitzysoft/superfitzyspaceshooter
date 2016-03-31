package com.fitzysoft.spaceshooter;

import carlfx.gameengine.GameWorld;
import carlfx.gameengine.SoundManager;
import carlfx.gameengine.Sprite;
import carlfx.gameengine.SpriteManager;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.logging.Logger;

/**
 * Created by James FitzGerald on 3/27/16.
 */
public class Enemy extends Sprite {
    private static Logger logger = Logger.getLogger("com.fitzysoft.sfs");

    private static final double enemyRotate = 3.0;
    private static final int enemySpeed = 7;

    private GameContext gameContext;

    // todo: Let's have a context object that contains the soundManager, scene, spriteManager etc...
    public Enemy(GameContext gameContext) {
        this.gameContext = gameContext;

        // Load one image.
        ImageView imageView = new ImageView();
        imageView.setImage(new Image(getClass().getClassLoader().getResource("enemy1.png").toExternalForm(), true));
        imageView.setCache(true);
        node = imageView;

        Scene scene = gameContext.getSfsGameWorld().getGameSurface();

        // todo: make it within a random location but not too close
        //
        node.setTranslateX(scene.getWidth());
        node.setTranslateY(scene.getHeight());
        //shipYSpeed = shipXSpeed = 1.5;
        node.setVisible(true);
    }

    @Override
    public void update() {
        // todo : make this more dynamic
        double enemySpeed = 5;
        double spin = 8.0;

        double a = gameContext.getPlayerShip().node.getTranslateX() - node.getTranslateX();
        double o = gameContext.getPlayerShip().node.getTranslateY() - node.getTranslateY();
        double h = Math.sqrt(a*a + o*o);
        double deltaX = a/h * enemySpeed;
        double deltaY = o/h * enemySpeed;
        node.setTranslateX(node.getTranslateX() + deltaX);
        node.setTranslateY(node.getTranslateY() + deltaY);

        // rotate for groovy effect
        //
        if (deltaX > 0) {
            node.setRotate(node.getRotate() + spin);
        } else {
            node.setRotate(node.getRotate() - spin);
        }
        //node.setRotate( node.getRotate() + spin ); //+ deltaX > 0 ? spin : spin*-1);
    }
}
