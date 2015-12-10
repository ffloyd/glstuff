import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class VoronoiDemo extends GLFWApplication {
    private SimpleVAO oneColorSquare;

    public VoronoiDemo() {
        super("Voronoi Diagrams Demo", 768, 768);
        setKeyCallback(new BasicKeyCallback());
    }

    @Override
    protected void beforeLoop() {
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        SimpleVBO square = new PointsVBO(new float[] {
                -1f, -1f,
                -1f,  1f,
                1f, -1f,
                1f,  1f,
                -1,  1f,
                1f, -1f,
        }, 2);

        ShaderProgram simpleProgram = new ShaderProgram(new Shader[] {
                new Shader("flat.vert",     GL20.GL_VERTEX_SHADER),
                new Shader("simple.frag",   GL20.GL_FRAGMENT_SHADER)
        });

        oneColorSquare = new SimpleVAO(square, simpleProgram);
        oneColorSquare.build();

        FloatArrayUBO squareColor = new FloatArrayUBO(new float[] {
                0.5f, 0.5f, 0.5f, 1.0f
        });

        squareColor.build(1);
        simpleProgram.bindUBO(squareColor, "Data");
    }

    @Override
    protected void eachFrame() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        oneColorSquare.draw();
    }

    public static void main(String[] args) {
        new VoronoiDemo().run();
    }
}
