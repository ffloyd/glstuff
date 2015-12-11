package io.github.ffloyd.glstuff.demos.voronoi_diagrams;

import io.github.ffloyd.glstuff.Helpers;
import io.github.ffloyd.glstuff.shaders.ShaderProgram;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Vector;

public class GeneratorManager {
    private Vector<Point>   points;
    private Vector<Point>   current;
    private Vector<Color>   colors;
    private Vector<Integer> endpoints;
    private ShaderProgram   program;

    public GeneratorManager(ShaderProgram program) {
        this.program = program;

        points      = new Vector<>();
        current     = new Vector<>();
        colors      = new Vector<>();
        endpoints   = new Vector<>();
    }

    public void addPoint(float x, float y) {
        current.add(new Point(x, y));
    }

    public void addFinalPoint(float x, float y) {
        addPoint(x, y);
        points.addAll(current);
        current.clear();

        colors.add(new Color());
        endpoints.add(points.size());

        upload();
    }

    private void upload() {
        float[] pointsData  = extractFloats(points);
        float[] colorsData  = extractFloats(colors);
        int[] endpointsData = endpoints.stream().mapToInt(Integer::intValue).toArray();

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
        program.setUniformVariable("endpoints", location -> {
            IntBuffer intBuffer = Helpers.generateIntBuffer(endpointsData);
            GL20.glUniform1iv(location, intBuffer);
        });
    }

    public void addRandomPointGenerator(float xRange, float yRange) {
        addFinalPoint((float)Math.random() * xRange, (float)Math.random() * yRange);
    }

    private <T extends FlatFloat> float[] extractFloats(Vector<T> vector) {
        return Helpers.convert(
                vector.stream().map(T::getFields).flatMap(Arrays::stream).mapToDouble(Float::doubleValue).toArray()
        );
    }
}
