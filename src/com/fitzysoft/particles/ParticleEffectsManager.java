package com.fitzysoft.particles;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by James FitzGerald on 4/24/16.
 * This class will represent a single pane and graphics context for the entire window
 */
public class ParticleEffectsManager extends Canvas {

    private List<TrackingEmitter> emitters = new ArrayList<>();
    private List<Particle> particles = new ArrayList<>();
    private GraphicsContext g;

    private int width;
    private int height;

    public ParticleEffectsManager(int width, int height) {
        super(width, height);
        this.width = width;
        this.height = height;

        //canvas = new Canvas(width, height);
        g = this.getGraphicsContext2D();
    };

    private class TrackingEmitter {
        Emitter emitter;
        DoubleProperty xProperty;
        DoubleProperty yProperty;

        public TrackingEmitter(Emitter emitter, DoubleProperty xProperty, DoubleProperty yProperty) {
            this.emitter = emitter;
            this.xProperty = xProperty;
            this.yProperty = yProperty;
        }
    }

    // add a particle emitter and have the x,y of the e
    public void addParticleEmitterWithOriginTracking(Emitter emitter, DoubleProperty xProperty, DoubleProperty yProperty) {
        emitters.add(new TrackingEmitter(emitter, xProperty, yProperty));
    }

    // call each frame
    public void update() {

        //g.setFill(Color.BLACK);
        g.setGlobalAlpha(1.0);
        g.setGlobalBlendMode(BlendMode.SRC_OVER);
        g.clearRect(0, 0, width, height);
        //g.fillRect(0, 0, 600, 600);

        for (Iterator<TrackingEmitter> it = emitters.iterator(); it.hasNext();) {
            TrackingEmitter trackingEmitter = it.next();
            particles.addAll(trackingEmitter.emitter.emit(trackingEmitter.xProperty.getValue(), trackingEmitter.yProperty.getValue()));
        }

        for (Iterator<Particle> it = particles.iterator(); it.hasNext();) {
            Particle p = it.next();
            p.update();
            if (p.isAlive()) {
                p.render(g);
            }
        }
    }
}
