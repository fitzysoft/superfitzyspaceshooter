package com.fitzysoft.particles;

import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James FitzGerald on 4/23/16.
 */
public class ExplosionEmitter extends Emitter {

    private int numParticles = 15; // todo: make configurable
    private double expiresIn = 0.25;
    private double baseRadius = 5;
    private double baseDeltaRange = 3;
    private double velocity = 16;
    private int step;
    private int stepTotal = 45;

    Color c = Color.rgb(240, 32, 147);

    public ExplosionEmitter() {
        reset();
    }

    public void reset() {
        step = 0;
    }

    public boolean finished() {
        return step >= stepTotal;
    }

    @Override
    public List<Particle> emit(double x, double y) {
        List<Particle> particleList = new ArrayList<>();

        if (step >= stepTotal) {
            return null;
        }

        for (int i = 0; i < numParticles; i++) {

            double xVel = velocity * (Math.random() - 0.5);
            double yVel = velocity * (Math.random() - 0.5);

            int colorDelta = (int)(0.15 * (double) step +  xVel);

            int r = (int) (217.0 - step * 0.25);
            if (r > 255) {
                r = 255;
            }
            int g = 15 + colorDelta;
            if (g > 255) {
                g = 255;
            }
            int b = 27 + colorDelta;
            if (b > 255) {
                b = 255;
            }

            Color color = Color.rgb(r, g, b);

            Particle p = new Particle(x + (Math.random()-0.5)*20, y + (Math.random()-0.5)*20,
                    new Point2D(xVel, yVel), expiresIn,
                    baseRadius + Math.random() * baseDeltaRange, color, BlendMode.SRC_OVER);

            particleList.add(p);
        }
        step++;
        return particleList;
    }
}
