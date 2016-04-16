package com.fitzysoft.spaceshooter;

import java.util.ArrayList;

/**
 * Created by James FitzGerald on 4/16/16.
 */
public class EnemyWave {

    ArrayList<Enemy> enemies;
    ArrayList<Enemy> deadEnemies;   // we have to clean them up after we have done all the sprite updates
    GameContext gameContext;

    public EnemyWave(GameContext gameContext) {
        this.gameContext = gameContext;
        this.enemies = new ArrayList<>();
        this.deadEnemies = new ArrayList<>();
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    // create the next wave of enemies
    public void createEnemies(int waveLevel) {
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

    // remove all the 'to be removed' enemies from the list
    public void cleanUpEnemies() {
        enemies.removeAll(deadEnemies);
        deadEnemies.clear();
    }
}
