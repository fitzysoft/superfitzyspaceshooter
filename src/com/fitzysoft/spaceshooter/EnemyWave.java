package com.fitzysoft.spaceshooter;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by James FitzGerald on 4/16/16.
 */
public class EnemyWave {

    private static Logger logger = Logger.getLogger("com.fitzysoft.sfs");

    private ArrayList<Enemy> enemies;
    private ArrayList<Enemy> deadEnemies;   // we have to clean them up after we have done all the sprite updates
    private GameContext gameContext;

    private boolean createNextWaveOnNextUpdate;

    public EnemyWave(GameContext gameContext) {
        this.gameContext = gameContext;
        this.enemies = new ArrayList<>();
        this.deadEnemies = new ArrayList<>();
        this.createNextWaveOnNextUpdate = false;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    // create the next wave of enemies
    public void createEnemies(int waveLevel) {
        logger.info("Creating enemy wave " + waveLevel);
        createNextWaveOnNextUpdate = false;
        // increase the number of enemies coming at a time with each wave
        for (int i = 0; i < waveLevel + 1; ++i) {
            Enemy enemy = new Enemy(gameContext);
            enemies.add(enemy);
            gameContext.getSfsGameWorld().getSpriteManager().addSprites(enemy);
            gameContext.getSfsGameWorld().getSceneNodes().getChildren().add(enemy.node);
        }
    }

    // mark this enemy as 'to be removed', and remove it from the scene graph
    public void destroyEnemy(Enemy enemy) {
        deadEnemies.add(enemy);
        gameContext.getSfsGameWorld().getSceneNodes().getChildren().removeAll(enemy.node);
        gameContext.getSfsGameWorld().enemyShot();
    }

    public boolean allDead() {
        // may be called before cleanup or after - this should work either way
        return deadEnemies.size() == enemies.size();
    }

    public boolean isCreateNextWaveOnNextUpdate() {
        return createNextWaveOnNextUpdate;
    }

    public void setCreateNextWaveOnNextUpdate() {
        createNextWaveOnNextUpdate = true;
    }

    // remove all the 'to be removed' enemies from the list
    public void cleanUpEnemies() {
        enemies.removeAll(deadEnemies);
        deadEnemies.clear();
    }
}
