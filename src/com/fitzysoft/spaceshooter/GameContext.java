package com.fitzysoft.spaceshooter;

import carlfx.gameengine.SoundManager;
import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Created by James FitzGerald on 3/31/16.
 */
public class GameContext {

    private SFSpaceShooterGame sfsGameWorld;
    private SoundManager soundManager;
    private PlayerShip playerShip;

    enum GameState {READY_TO_START, LEVEL_STARTED, LEVEL_ENDED, PLAYER_KILLED};

    // todo: make this a property so we can bind to it ??
    private GameState gameState = GameState.READY_TO_START;

    EnemyWave enemyWave;

    // todo: put things like score, level, etc into a class of its own
    // so we can persist it, and switch it out from player 1 to player 2 etc...

    // todo: shift this to be a property too
    int currentLevel;

    private IntegerProperty scoreProperty;

    public IntegerProperty getScoreProperty() {return scoreProperty;}

    public void increaseScoreBy(int value) {
        //scoreProperty.add(value);
        scoreProperty.set(value + scoreProperty.intValue());

    }


    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public GameContext() {
        enemyWave = new EnemyWave(this);
        currentLevel = 0;
        scoreProperty = new SimpleIntegerProperty();
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
