package com.fitzysoft.spaceshooter;

import carlfx.gameengine.SoundManager;
import java.util.ArrayList;

/**
 * Created by James FitzGerald on 3/31/16.
 */
public class GameContext {

    SFSpaceShooterGame sfsGameWorld;
    SoundManager soundManager;
    PlayerShip playerShip;

    //    ArrayList<Enemy> enemies;
//    ArrayList<Enemy> deadEnemies;   // we have to clean them up after we have done all the sprite updates
    EnemyWave enemyWave;

    int currentLevel;

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public GameContext() {
        enemyWave = new EnemyWave(this);
        currentLevel = 0;
    }

    public EnemyWave getEnemyWave() {
        return enemyWave;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public void setSoundManager(SoundManager soundManager) {
        this.soundManager = soundManager;
    }

    public PlayerShip getPlayerShip() {
        return playerShip;
    }

    public void setPlayerShip(PlayerShip playerShip) {
        this.playerShip = playerShip;
    }

    public SFSpaceShooterGame getSfsGameWorld() {
        return sfsGameWorld;
    }

    public void setSfsGameWorld(SFSpaceShooterGame sfsGameWorld) {
        this.sfsGameWorld = sfsGameWorld;
    }
}
