package io.github.ffloyd.glstuff.demos.voronoi_diagrams;

import io.github.ffloyd.glstuff.Helpers;
import io.github.ffloyd.glstuff.LambdaKeyCallback;
import io.github.ffloyd.glstuff.SimpleGLFWApplication;
import io.github.ffloyd.glstuff.VAO.SimpleVAO;
import io.github.ffloyd.glstuff.VBO.PointsVBO;
import io.github.ffloyd.glstuff.shaders.Shader;
import io.github.ffloyd.glstuff.shaders.ShaderProgram;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.util.Vector;

public class VoronoiDiagrams extends SimpleGLFWApplication {
    SimpleVAO       surface;
    ShaderProgram   program;

    VoronoiDiagrams() {
        super("Voronoi Diagrams", 768, 768);
        setKeyCallback(new LambdaKeyCallback(this::processKey));
    }

    private void processKey(int keyCode) {
        if (keyCode == GLFW.GLFW_KEY_P) {
            program.setUniformVariable("seeds_count", location -> GL20.glUniform1i(location, 1));
        }
    }

    @Override
    protected void beforeLoop() {
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        PointsVBO quad = new PointsVBO(Helpers.generateQuad(
                1.0f, 1.0f, -1.0f, -1.0f
        ), 2, GL15.GL_STATIC_DRAW);
        quad.build();

        Shader flatDrawShader = new Shader("flat.vert", GL20.GL_VERTEX_SHADER);
        flatDrawShader.build();

        Shader voronoiShader = new Shader("voronoi.frag", GL20.GL_FRAGMENT_SHADER);
        voronoiShader.build();

        program = new ShaderProgram(new Shader[] {
                flatDrawShader, voronoiShader
        });
        program.build();

        surface = new SimpleVAO(quad, program);
        surface.build();

        program.setUniformVariable("seeds_count", location -> GL20.glUniform1i(location, 2));
        program.setUniformVariable("seeds", location -> {
            float[] seeds = new float[] {
                    200f, 200f,
                    100f, 100f
            };
            FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(seeds.length);
            floatBuffer.put(seeds);
            floatBuffer.flip();
            GL20.glUniform2fv(location, floatBuffer);
        });
        program.setUniformVariable("colors", location -> {
            float[] seeds = new float[] {
                    0.0f, 0.5f, 0.0f,
                    0.5f, 0.0f, 0.0f,
            };
            FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(seeds.length);
            floatBuffer.put(seeds);
            floatBuffer.flip();
            GL20.glUniform3fv(location, floatBuffer);
        });
    }

    @Override
    protected void eachFrame() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        surface.draw();
    }

    public static void main(String[] args) {
        new VoronoiDiagrams().run();
    }
}
