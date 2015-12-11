package io.github.ffloyd.glstuff.demos.voronoi_diagrams;

import io.github.ffloyd.glstuff.Helpers;
import io.github.ffloyd.glstuff.LambdaKeyCallback;
import io.github.ffloyd.glstuff.LambdaMouseButtonCallback;
import io.github.ffloyd.glstuff.SimpleGLFWApplication;
import io.github.ffloyd.glstuff.VAO.SimpleVAO;
import io.github.ffloyd.glstuff.VBO.PointsVBO;
import io.github.ffloyd.glstuff.shaders.Shader;
import io.github.ffloyd.glstuff.shaders.ShaderProgram;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

public class VoronoiDiagrams extends SimpleGLFWApplication {
    private SimpleVAO       surface;
    private ShaderProgram   program;
    private GeneratorManager generatorManager;

    public static final int WINDOW_WIDTH   = 768;
    public static final int WINDOW_HEIGHT  = 768;

    VoronoiDiagrams() {
        super("Voronoi Diagrams", WINDOW_WIDTH, WINDOW_HEIGHT);
        setKeyCallback(new LambdaKeyCallback(this::processKey));
        setMouseButtonCallback(new LambdaMouseButtonCallback(this::processLeftButton, this::processRightButton));
    }

    private void processLeftButton(Double x, Double y) {
        generatorManager.addFinalPoint(x.floatValue(), (float)WINDOW_HEIGHT - y.floatValue());
    }

    private void processRightButton(Double x, Double y) {
        generatorManager.addPoint(x.floatValue(), (float)WINDOW_HEIGHT - y.floatValue());
    }

    private void processKey(int keyCode) {
        if (keyCode == GLFW.GLFW_KEY_P) {
            generatorManager.addRandomPointGenerator((float)WINDOW_WIDTH, (float)WINDOW_HEIGHT);
        }
        if (keyCode == GLFW.GLFW_KEY_R) {
            generatorManager.reset();
            generatorManager.addRandomPointGenerator((float)WINDOW_WIDTH, (float)WINDOW_HEIGHT);
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

        generatorManager = new GeneratorManager(program);
        generatorManager.addRandomPointGenerator((float)WINDOW_WIDTH, (float)WINDOW_HEIGHT);
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
