package com.fitzysoft.spaceshooter;

import javafx.geometry.Bounds;

import java.util.ArrayList;

/**
 * Created by James FitzGerald on 12/24/16.
 */
public class BurstySwarm {
    ArrayList<Enemy> swarm;

    public BurstySwarm(GameContext gameContext, int count, Bounds playerBounds) {
        swarm = new ArrayList<>(count);
        double width = gameContext.getSfsGameWorld().getGameSurface().getWidth();
        double height = gameContext.getSfsGameWorld().getGameSurface().getHeight();

        double playerX = playerBounds.getMinX() + playerBounds.getWidth()/2;
        double playerY = playerBounds.getMinY() + playerBounds.getHeight()/2;

        // we want to be on screen but not too close to the player

        double randomXOffset = (Math.random() - 0.5) * width/2;
        double randomYOffset = (Math.random() - 0.5) * height/2;

        if (playerX > width/2) {
            randomXOffset = - randomXOffset;
        }
        if (playerY > height/2) {
            randomYOffset = -randomYOffset;
        }

        double x = width/2 + randomXOffset;
        double y = height/2 + randomYOffset;
        for (int i = 0; i < count; i++) {
            swarm.add(new EnemyBursty(gameContext, x, y));
        }
    }

    public ArrayList<Enemy> getEnemies() {
        return swarm;
    }
}
