package io.github.ffloyd.glstuff.demos.voronoi_diagrams;

import io.github.ffloyd.glstuff.Helpers;
import io.github.ffloyd.glstuff.shaders.ShaderProgram;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.Vector;
import java.util.function.Function;

public class GeneratorManager {
    private Vector<Point> points;
    private ShaderProgram program;

    public GeneratorManager(ShaderProgram program) {
        this.program = program;
        points = new Vector<>();
    }

    public void addPoint(float x, float y) {
        points.add(new Point(x, y));
        float[] seeds   = extractFloats(Point::getCoords);
        float[] colors  = extractFloats(Point::getColor);

        program.setUniformVariable("seeds_count", location -> GL20.glUniform1i(location, points.size()));
        program.setUniformVariable("seeds", location -> {
            FloatBuffer floatBuffer = Helpers.generateFloatBuffer(seeds);
            GL20.glUniform2fv(location, floatBuffer);
        });
        program.setUniformVariable("colors", location -> {
            FloatBuffer floatBuffer = Helpers.generateFloatBuffer(colors);
            GL20.glUniform3fv(location, floatBuffer);
        });
    }



    public void addRandomPoint(float xRange, float yRange) {
        addPoint((float)Math.random() * xRange, (float)Math.random() * yRange);
    }

    private float[] extractFloats(Function<Point, Float[]> mapper) {
        return Helpers.convert(
                points.stream().map(mapper).flatMap(Arrays::stream).mapToDouble(Float::doubleValue).toArray()
        );
    }
}
