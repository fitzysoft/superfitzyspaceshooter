package com.fitzysoft.spaceshooter;

import carlfx.gameengine.Sprite;
import com.fitzysoft.particles.ExplosionEmitter;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
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

    // todo: the playership has pretty much the same states, we should consolidate
    enum State {ALIVE, DYING, DEAD, HEAVEN };
    public State state;
    // todo: make state an observable property


    protected GameContext gameContext;

    public Enemy(GameContext gameContext) {
        this.gameContext = gameContext;
        this.state = State.ALIVE;
    }



    // Some cool effect
    protected void dyingUpdate() {

    }



    @Override
    public void update() {

        switch (state) {
            case ALIVE:
                // Check if we hit the player ship
                //
                if (simpleCollisionCheck(gameContext.getPlayerShip())) {
                    logger.info("We hit the player!!!");
                    gameContext.getPlayerShip().enemyHitMe();
                    explode(false);
                }
                break;
            case DYING:
                dyingUpdate();
                break;
            case DEAD:
                // removes from the gameworld sprite manager (which I may be removing)
                handleDeath(gameContext.getSfsGameWorld());
                gameContext.enemyWave.destroyEnemy(this);
                state = State.HEAVEN;
                break;
            case HEAVEN:
                // right here, proof
                break;
            default:
                // nada
        }
    }

    public void explode(boolean playSound) {
        // We will go to a dying state where we are exploding
        state = State.DYING;
    }
}
