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
    ArrayList<Enemy> enemies;
    int currentLevel;

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public GameContext() {
        enemies = new ArrayList<>();
        currentLevel = 0;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
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
