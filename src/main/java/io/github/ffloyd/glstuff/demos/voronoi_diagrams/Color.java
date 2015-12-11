package io.github.ffloyd.glstuff.demos.voronoi_diagrams;

import java.util.Random;

public class Color implements FlatFloat {
    public float r;
    public float g;
    public float b;

    private static Random random = new Random();

    public Color(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Color() {
        r = 0.4f + random.nextFloat() * 0.6f;
        g = 0.4f + random.nextFloat() * 0.6f;
        b = 0.4f + random.nextFloat() * 0.6f;
    }

    @Override
    public Float[] getFields() {
        return new Float[] {r, g, b};
    }
}
