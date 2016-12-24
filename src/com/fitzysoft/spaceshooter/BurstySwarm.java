package com.fitzysoft.spaceshooter;

import java.util.ArrayList;

/**
 * Created by James FitzGerald on 12/24/16.
 */
public class BurstySwarm {
    ArrayList<Enemy> swarm;

    public BurstySwarm(GameContext gameContext, int count) {
        swarm = new ArrayList<>(count);
        double width = gameContext.getSfsGameWorld().getGameSurface().getWidth();
        double height = gameContext.getSfsGameWorld().getGameSurface().getHeight();

        double x = width/2;
        double y = height/2;

        double randomXOffset = (Math.random() - 0.5) * width;
        double randomYOffset = (Math.random() - 0.5) * height;

        x += randomXOffset;
        y += randomYOffset;
        for (int i = 0; i < count; i++) {
            swarm.add(new EnemyBursty(gameContext, x, y));
        }
    }

    public ArrayList<Enemy> getEnemies() {
        return swarm;
    }
}
