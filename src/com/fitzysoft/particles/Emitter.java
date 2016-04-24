package com.fitzysoft.particles;

import java.util.List;

/**
 * Created by James FitzGerald on 4/23/16.
 */
public abstract class Emitter {
    // todo: might want to remove the x, y aspect and make it part of the constructor of the sub classes
    public abstract List<Particle> emit(double x, double y);

    public abstract void reset();
}
