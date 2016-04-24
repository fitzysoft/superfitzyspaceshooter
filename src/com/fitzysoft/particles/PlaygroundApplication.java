package com.fitzysoft.particles;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jamesfitzgerald on 4/23/16.
 */
public class PlaygroundApplication extends Application {

    // todo: shift this to its own class
    private List<Particle> particles = new ArrayList<>();
    private Emitter emitter = new ExplosionEmitter();

    private GraphicsContext g;

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(600, 600);

        Canvas canvas = new Canvas(600, 600);
        g = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        return root;
    }

    private void onUpdate() {
        g.setFill(Color.BLACK);
        g.setGlobalAlpha(1.0);
        g.setGlobalBlendMode(BlendMode.SRC_OVER);
        g.fillRect(0, 0, 600, 600);

        List<Particle> newParticles = emitter.emit(300, 300);
        if (newParticles != null) {
            particles.addAll(newParticles);
        }

        for (Iterator<Particle> it = particles.iterator(); it.hasNext();) {
            Particle p = it.next();
            p.update();
            if (!p.isAlive()) {
                it.remove();
                continue;
            }
            p.render(g);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));

        primaryStage.show();

        EventHandler eventHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                particles.clear();
                emitter.reset();
            }
        };
        primaryStage.getScene().setOnKeyPressed(eventHandler);

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        animationTimer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
