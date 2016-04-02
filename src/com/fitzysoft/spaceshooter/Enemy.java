package com.fitzysoft.spaceshooter;

import carlfx.gameengine.Sprite;
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
        node.setRotate(node.getRotate() + ((deltaX > 0) ? spin : (spin * -1)));

        // Check if we hit the player ship
        //
        if (simpleCollisionCheck(gameContext.getPlayerShip())) {
            logger.info("We hit the player!!!");
        }
    }

    public void explode() {
        // todo: play sound
        // todo: animate explosion
        // todo: remove from scene after animation completes
        logger.info("Enemy goes kaboom!");
        gameContext.getEnemies().remove(this);

        // removes from the gameworld sprite manager (which I may be removing)
        handleDeath(gameContext.getSfsGameWorld());

        // let the game object know about the event
        //
        gameContext.getSfsGameWorld().enemyShot(this);

        // remove from the scene
        // todo: we would really want to do this after the death animation completes
        gameContext.getSfsGameWorld().getSceneNodes().getChildren().removeAll(node);
    }
}
