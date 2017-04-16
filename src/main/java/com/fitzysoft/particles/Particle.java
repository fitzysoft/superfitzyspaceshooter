package com.fitzysoft.particles;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Paint;

/**
 * Created by jamesfitzgerald on 4/23/16.
 */
public class Particle {
    private double x;
    private double y;
    private Point2D velocity;
    private double life = 1.0;
    private double decay;
    private double radius;
    private Paint colour;
    private BlendMode blendMode;

    public Particle(double x, double y, Point2D velocity, double expiresInSecs, double radius, Paint colour, BlendMode blendMode) {
        this.x = x;
        this.y = y;
        this.velocity = velocity;
        this.decay = 0.016 / expiresInSecs; // assumes fps is 60
        this.radius = radius;
        this.colour = colour;
        this.blendMode = blendMode;

    }

    public void update() {
        x += velocity.getX();
        y += velocity.getY();
        life -= decay;

    }

    public boolean isAlive() {
        return life > 0;
    }
    public void render(GraphicsContext g) {
        // we get a nice decay effect
        g.setGlobalAlpha(life);
        g.setGlobalBlendMode(blendMode);
        g.setFill(colour);
        g.fillOval(x, y, radius, radius);
    }
}
