package io.github.ffloyd.glstuff.demos.voronoi_diagrams;

import java.util.Random;

public class Point {
    public float x;
    public float y;

    public float r;
    public float g;
    public float b;

    private static Random random = new Random();

    public Point(float x, float y, float r, float g, float b) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
        r = random.nextFloat();
        g = random.nextFloat();
        b = random.nextFloat();
    }

    public Float[] getCoords() {
        return new Float[] {x, y};
    }

    public Float[] getColor() {
        return new Float[] {r, g, b};
    }
}
