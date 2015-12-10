package io.github.ffloyd.glstuff.demos.voronoi_diagrams;

import io.github.ffloyd.glstuff.Helpers;
import io.github.ffloyd.glstuff.shaders.ShaderProgram;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.Vector;

public class PointManager {
    private Vector<Point> points;
    private ShaderProgram program;

    public PointManager(ShaderProgram program) {
        this.program = program;
        points = new Vector<>();
    }

    public void addPoint(float x, float y) {
        points.add(new Point(x, y));
        float[] seeds   = Helpers.convert(
                points.stream().map(Point::getCoords).flatMap(Arrays::stream).mapToDouble(Float::doubleValue).toArray()
        );
        float[] colors  = Helpers.convert(
                points.stream().map(Point::getColor).flatMap(Arrays::stream).mapToDouble(Float::doubleValue).toArray()
        );

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
}
