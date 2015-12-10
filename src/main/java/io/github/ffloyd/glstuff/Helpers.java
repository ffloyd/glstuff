package io.github.ffloyd.glstuff;

public class Helpers {
    public static float[] generateQuad(float right, float top, float left, float bottom) {
        return new float[] {
                left, bottom,
                left, top,
                right, bottom,

                right, top,
                left, top,
                right, bottom
        };
    }
}
