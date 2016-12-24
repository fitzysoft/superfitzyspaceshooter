package com.fitzysoft.spaceshooter;

import com.fitzysoft.particles.ExplosionEmitter;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by James FitzGerald on 8/7/16.
 */
public class EnemyRedNoob extends Enemy {

    // frame rate should be 30 fps
    private static int framesToFadeOut = 60;
    private int fadeOutFrameCount = 0;

    public EnemyRedNoob(GameContext gameContext) {
        super(gameContext);
        // Load one image.
        ImageView imageView = new ImageView();
        imageView.setImage(new Image(getClass().getClassLoader().getResource("enemy1.png").toExternalForm(), true));
        imageView.setCache(true);
        node = imageView;
        positionEnemy();
        node.setVisible(true);

        gameContext.getSoundManager().loadSoundEffects("enemy_death", getClass().getClassLoader().getResource(
                "enemy_explode.wav"));
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

    double currentSpeed = 0;
    double accelleration = 0.25;
    @Override
    public void update() {

        // todo : make this more dynamic
        double enemySpeed = 5;
        double spin = 8.0;

        // todo: shift this somewhere it will only need evaluating once
        if (gameContext.getGameState() == GameContext.GameState.LEVEL_STARTED) {
            enemySpeed = 5;
        } else {
            enemySpeed = 1;
        }

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
