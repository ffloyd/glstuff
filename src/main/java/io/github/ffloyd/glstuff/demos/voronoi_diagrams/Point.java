package io.github.ffloyd.glstuff.demos.voronoi_diagrams;

import java.util.Random;

public class Point implements FlatFloat {
    public float x;
    public float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Float[] getFields() {
        return new Float[] {x, y};
    }
}
