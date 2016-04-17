package com.fitzysoft.spaceshooter;

import carlfx.gameengine.SoundManager;

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

    int currentLevel;

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
