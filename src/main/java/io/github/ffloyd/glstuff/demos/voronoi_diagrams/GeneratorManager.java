package io.github.ffloyd.glstuff.demos.voronoi_diagrams;

import io.github.ffloyd.glstuff.Helpers;
import io.github.ffloyd.glstuff.shaders.ShaderProgram;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Vector;
import java.util.function.Function;

public class GeneratorManager {
    static final int EUCLID_METRIC  = 0;
    static final int L_1_METRIC     = 1;
    static final int L_INF_METRIC   = 2;

    private Vector<Point>   points;
    private Vector<Color>   colors;
    private Vector<Integer> endpoints;
    private Vector<Float>   multWeights;
    private Vector<Float>   addWeights;
    private boolean         beginNewGenerator;
    private int             metric;
    private ShaderProgram   program;

    private float windowWidth;
    private float windowHeight;

    public GeneratorManager(ShaderProgram program, float windowWidth, float windowHeight) {
        this.program        = program;
        this.windowWidth    = windowWidth;
        this.windowHeight   = windowHeight;

        reset();
    }

    public void reset() {
        points              = new Vector<>();
        colors              = new Vector<>();
        endpoints           = new Vector<>();
        multWeights         = new Vector<>();
        addWeights          = new Vector<>();
        beginNewGenerator   = true;
        metric              = EUCLID_METRIC;

        addRandomPointGenerator();
    }

    public void addPoint(float x, float y) {
        if (beginNewGenerator) {
            endpoints.add(points.size());
            colors.add(new Color());
            multWeights.add(1.0f);
            addWeights.add(0.0f);
            beginNewGenerator = false;
        }

        points.add(new Point(x, y));
        updateLast(endpoints, value -> value + 1);

        upload();
    }

    public void addFinalPoint(float x, float y) {
        addPoint(x, y);

        beginNewGenerator = true;
    }

    public void addRandomPointGenerator() {
        addFinalPoint((float)Math.random() * windowWidth, (float)Math.random() * windowHeight);
    }

    public void increaseMultWeight() {
        updateLast(multWeights, value -> value / 0.9f);
        upload();
    }

    public void decreaseMultWeight() {
        updateLast(multWeights, value -> value * 0.9f);
        upload();
    }

    public void increaseAddWeight() {
        updateLast(addWeights, value -> value + 5f);
        upload();
    }

    public void decreaseAddWeight() {
        updateLast(addWeights, value -> value - 5f);
        upload();
    }

    public void changeMetric() {
        metric = (metric + 1) % 3;
        upload();
    }

    private void upload() {
        float[] pointsData      = extractFloats(points);
        float[] colorsData      = extractFloats(colors);
        float[] multWeightsData = Helpers.convert(multWeights.stream().mapToDouble(Float::doubleValue).toArray());
        float[] addWeightsData  = Helpers.convert(addWeights.stream().mapToDouble(Float::doubleValue).toArray());
        int[] endpointsData     = endpoints.stream().mapToInt(Integer::intValue).toArray();

        program.setUniformVariable("metric", location -> GL20.glUniform1i(location, metric));

        program.setUniformVariable("points_count", location -> GL20.glUniform1i(location, points.size()));
        program.setUniformVariable("points", location -> {
            FloatBuffer floatBuffer = Helpers.generateFloatBuffer(pointsData);
            GL20.glUniform2fv(location, floatBuffer);
        });

        program.setUniformVariable("generators_count", location -> GL20.glUniform1i(location, endpoints.size()));
        program.setUniformVariable("colors", location -> {
            FloatBuffer floatBuffer = Helpers.generateFloatBuffer(colorsData);
            GL20.glUniform3fv(location, floatBuffer);
        });
        program.setUniformVariable("mult_weights", location -> {
            FloatBuffer floatBuffer = Helpers.generateFloatBuffer(multWeightsData);
            GL20.glUniform1fv(location, floatBuffer);
        });
        program.setUniformVariable("add_weights", location -> {
            FloatBuffer floatBuffer = Helpers.generateFloatBuffer(addWeightsData);
            GL20.glUniform1fv(location, floatBuffer);
        });
        program.setUniformVariable("endpoints", location -> {
            IntBuffer intBuffer = Helpers.generateIntBuffer(endpointsData);
            GL20.glUniform1iv(location, intBuffer);
        });
    }

    private <T extends FlatFloat> float[] extractFloats(Vector<T> vector) {
        return Helpers.convert(
                vector.stream().map(T::getFields).flatMap(Arrays::stream).mapToDouble(Float::doubleValue).toArray()
        );
    }

    private <T> void updateLast(Vector<T> vector, Function<T, T> mutator) {
        T oldValue = vector.lastElement();
        T newValue = mutator.apply(oldValue);
        vector.set(vector.size() - 1, newValue);
    }
}
