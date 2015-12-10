package io.github.ffloyd.glstuff;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

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

    public static FloatBuffer generateFloatBuffer(float[] data) {
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(data.length);
        floatBuffer.put(data);
        floatBuffer.flip();
        return floatBuffer;
    }

    public static float[] convert(double[] data) {
        float[] result = new float[data.length];
        for (int i = 0; i < data.length; ++i) result[i] = (float) data[i];
        return result;
    }
}
