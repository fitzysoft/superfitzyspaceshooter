package com.fitzysoft.spaceshooter;

import com.fitzysoft.particles.ExplosionEmitter;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;


/**
 * Created by James FitzGerald on 8/21/16.
 */
public class EnemyBursty extends Enemy {


    private static int framesToFadeOut = 60;
    private int fadeOutFrameCount = 0;

    private static int burstDuration = 60;
    private int burstCount = 0;

    private double dx;
    private double dy;

    double currentSpeed = 0;
    double accelleration = 0.1;
    private double initialSpeed = 7;
    double tolerance = 70;

    public EnemyBursty(GameContext gameContext, double x, double y) {
        super(gameContext);

        //   Load one image.
        ImageView imageView = new ImageView();
        imageView.setImage(new Image(getClass().getClassLoader().getResource("bursty-enemy.png").toExternalForm(), true));
        imageView.setCache(true);
        node = imageView;
        positionEnemy(x, y);
        node.setVisible(true);

        gameContext.getSoundManager().loadSoundEffects("enemy_death", getClass().getClassLoader().getResource(
                "enemy_explode.wav"));

    }

    private void positionEnemy(double x, double y) {

        // Currently I make sure you are off to the side at a random location
        //
        Scene scene = gameContext.getSfsGameWorld().getGameSurface();

        double randomXOffset = (Math.random() - 0.5) * 5; //1000;
        double randomYOffset = (Math.random() - 0.5) * 5; //800;
        x += randomXOffset;
        y += randomYOffset;

        node.setTranslateX(x);
        node.setTranslateY(y);
    }

    // Some cool effect
    protected void dyingUpdate() {
        fadeOutFrameCount++;
        if (fadeOutFrameCount >= framesToFadeOut) {
            state = State.DEAD;
        }

        // todo: make it some cool effect, for now I might just play with the transparency
        // todo: I am experimenting here
        SuperFitzySpriteEffects.blurryFadeOut(node, fadeOutFrameCount, framesToFadeOut);
    }

    private int compareXLine(Node playerShipNode) {
        Point2D topLeft = node.localToScene(0,0);
        Point2D otherTopLeft = playerShipNode.localToScene(0, 0);
        boolean misses = topLeft.getX() + node.getBoundsInLocal().getWidth() < otherTopLeft.getX() ||
                topLeft.getX() > otherTopLeft.getX() +playerShipNode.getBoundsInLocal().getWidth();
        int rc = 0;
        if (!misses) {
            // todo: does not consider width/height
            rc = otherTopLeft.getY() > topLeft.getY() ? 1 : -1;
        }
        return rc;
    }

    private int compareYLine(Node playerShipNode) {
        Point2D topLeft = node.localToScene(0,0);
        Point2D otherTopLeft = playerShipNode.localToScene(0, 0);
        boolean misses = topLeft.getY() + node.getBoundsInLocal().getHeight() < otherTopLeft.getY() ||
                topLeft.getY() > otherTopLeft.getY() + playerShipNode.getBoundsInLocal().getHeight();
        int rc = 0;
        if (!misses) {
            // todo: does not consider width/height
            rc = (otherTopLeft.getX() > topLeft.getX() ? 1 : -1);
        }
        return rc;
    }

    private void jiggle() {
        double x = node.getTranslateX() + (0.5 - Math.random())*20;
        double y = node.getTranslateY() + (0.5 - Math.random())*20;
        node.setTranslateX(x);
        node.setTranslateY(y);
    }


    @Override
    public void update() {

        // When the player crosses the vertical or horizontal line we will burst in that direction
        //
        Node playerShipNode = gameContext.getPlayerShip().node;

        if (burstCount > 0) {
            burstCount++;
            if (burstCount >= burstDuration) {
                burstCount = 0;
                dx = dy = 0.0;
            } else {
                // linear fade out
                //dx = dx * (burstDuration - burstCount)/burstDuration;
                //dy = dy * (burstDuration - burstCount)/burstDuration;

                node.setTranslateX(node.getTranslateX() + dx);
                node.setTranslateY(node.getTranslateY() + dy);

            }
        } else {

            int xl = compareXLine(playerShipNode);
            if (xl != 0) {
                dy = xl * initialSpeed;
                burstCount = 1;
            }

            int yl = compareYLine(playerShipNode);
            if (yl != 0) {
                dx = yl * initialSpeed;
                burstCount = 1;
            }

            jiggle();
        }



        // call super class to handle base update logic
        super.update();
    }

    public void explode(boolean playSound) {
        // play sound
        if (playSound) {
            gameContext.getSoundManager().playSound("enemy_death");
        }

        gameContext.increaseScoreBy(20);

        DoubleBinding x = node.translateXProperty().add( 0.5 * node.getBoundsInParent().getWidth());
        DoubleBinding y = node.translateYProperty().add( 0.5 * node.getBoundsInParent().getHeight());
        gameContext.getParticleEffectsManager().addParticleEmitterWithOriginTracking(new ExplosionEmitter(), x, y);

        super.explode(playSound);
    }
}
