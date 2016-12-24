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
        if (waveLevel < 3) {
            createEnemy(EnemyType.RedNoob, waveLevel+1);
        } else if (waveLevel > 3 && waveLevel < 6) {
            createEnemy(EnemyType.RedNoob, 1);
            createEnemy(EnemyType.Bursty, waveLevel);
        } else {
            createEnemy(EnemyType.RedNoob, waveLevel / 5);
            createEnemy(EnemyType.Bursty, waveLevel);
        }
    }
    private enum EnemyType { RedNoob, Bursty}
    private void createEnemy(EnemyType type, int count) {
        // todo: this is not written in an optimal way
        switch (type) {
            case RedNoob:
                for (int i = 0; i < count; i++) {
                    addEnemy(new EnemyRedNoob(gameContext));
                }
                break;
            case Bursty:
                BurstySwarm swarm = new BurstySwarm(gameContext, count);
                for (Enemy enemy:swarm.getEnemies()) {
                    addEnemy(enemy);
                }
        }
    }

    private void addEnemy(Enemy enemy) {
        enemies.add(enemy);
        gameContext.getSfsGameWorld().getSpriteManager().addSprites(enemy);
        gameContext.getSfsGameWorld().getSceneNodes().getChildren().add(enemy.node);
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
