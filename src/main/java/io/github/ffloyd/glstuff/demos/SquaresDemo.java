package io.github.ffloyd.glstuff.demos;

import io.github.ffloyd.glstuff.BasicKeyCallback;
import io.github.ffloyd.glstuff.SimpleGLFWApplication;
import io.github.ffloyd.glstuff.VAO.SimpleVAO;
import io.github.ffloyd.glstuff.VBO.PointsVBO;
import io.github.ffloyd.glstuff.shaders.Shader;
import io.github.ffloyd.glstuff.shaders.ShaderProgram;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

public class SquaresDemo extends SimpleGLFWApplication {
    private ShaderProgram program;

    private SimpleVAO firstSquare;
    private SimpleVAO secondSquare;

    public SquaresDemo() {
        super("The Green Square", 768, 768);
        setKeyCallback(new BasicKeyCallback());
    }

    private float[] generateQuad(float right, float top, float left, float bottom) {
        return new float[] {
                left, bottom,
                left, top,
                right, bottom,

                right, top,
                left, top,
                right, bottom
        };
    }

    @Override
    protected void beforeLoop() {
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        Shader flatDrawShader = new Shader("flat.vert",         GL20.GL_VERTEX_SHADER);
        Shader oneColorShader = new Shader("one_color.frag",    GL20.GL_FRAGMENT_SHADER);
        flatDrawShader.build();
        oneColorShader.build();

        program = new ShaderProgram(new Shader[] {
                flatDrawShader, oneColorShader
        });
        program.build();

        PointsVBO firstQuad = new PointsVBO(generateQuad(
                0.9f, 0.9f, 0.1f, 0.1f
        ), 2, GL15.GL_STATIC_DRAW);
        firstQuad.build();

        PointsVBO secondQuad = new PointsVBO(generateQuad(
                -0.1f, -0.1f, -0.9f, -0.9f
        ), 2, GL15.GL_STATIC_DRAW);
        secondQuad.build();

        firstSquare     = new SimpleVAO(firstQuad, program);
        secondSquare    = new SimpleVAO(secondQuad, program);

        firstSquare.build();
        secondSquare.build();
    }

    @Override
    protected void eachFrame() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        program.setUniformVariable("color", location -> GL20.glUniform4f(location, 0.0f, 0.5f, 0.0f, 1.0f));
        firstSquare.draw();

        program.setUniformVariable("color", location -> GL20.glUniform4f(location, 0.5f, 0.0f, 0.0f, 1.0f));
        secondSquare.draw();
    }

    public static void main(String[] args) {
        new SquaresDemo().run();
    }
}
