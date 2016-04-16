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

    enum State {ALIVE, DYING, DEAD };
    private State state;

    private GameContext gameContext;

    public Enemy(GameContext gameContext) {
        this.gameContext = gameContext;
        this.state = State.ALIVE;

        // Load one image.
        ImageView imageView = new ImageView();
        imageView.setImage(new Image(getClass().getClassLoader().getResource("enemy1.png").toExternalForm(), true));
        imageView.setCache(true);
        node = imageView;

        positionEnemy();

        //shipYSpeed = shipXSpeed = 1.5;
        node.setVisible(true);
    }

    private void positionEnemy() {

        // Currently I make sure you are off to the side at a random location
        //
        Scene scene = gameContext.getSfsGameWorld().getGameSurface();

        double randomXOffset = Math.random() * scene.getWidth();
        double randomYOffset = Math.random() * scene.getHeight();

        double x;
        double y;

        if (randomXOffset < scene.getWidth()/2) {
            x = 0 - randomXOffset;
        } else {
            x = scene.getWidth() + randomXOffset;
        }

        if (randomYOffset < scene.getHeight()/2) {
            y = 0 - randomYOffset;
        } else {
            y = scene.getHeight() + randomYOffset;
        }

        node.setTranslateX(x);
        node.setTranslateY(y);
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

        switch (state) {
            case ALIVE:
                // Check if we hit the player ship
                //
                if (simpleCollisionCheck(gameContext.getPlayerShip())) {
                    logger.info("We hit the player!!!");
                    // todo: Destroy the player or reduce health etc...
                }
                break;
            case DYING:
                // todo: split it apart
                break;
            case DEAD:
                // removes from the gameworld sprite manager (which I may be removing)
                handleDeath(gameContext.getSfsGameWorld());
                gameContext.enemyWave.destroyEnemy(this);

            default:
                // nada
        }
    }

    public void explode() {
        // todo: play sound
        // todo: animate explosion
        // todo: remove from scene after animation completes
        logger.info("Enemy goes kaboom!");
        //gameContext.getEnemies().remove(this);

        // todo: We will go to a dying state where we are exploding
        state = State.DEAD;

        // remove from the scene
        // todo: we would really want to do this after the death animation completes
        //gameContext.getSfsGameWorld().getSceneNodes().getChildren().removeAll(node);
    }
}
