import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class VoronoiDemo extends GLFWApplication {

    private SimpleFloatVBO triangle;

    private ShaderProgram shaderProgram;


    public VoronoiDemo() {
        super("Voronoi Diagrams Demo", 1024, 768);
        setKeyCallback(new BasicKeyCallback());

        shaderProgram = new ShaderProgram(new Shader[] {
                new Shader("flat.vert",     GL20.GL_VERTEX_SHADER),
                new Shader("simple.frag",   GL20.GL_FRAGMENT_SHADER)
        });
    }

    @Override
    protected void beforeLoop() {
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);


        triangle = new SimpleFloatVBO(new float[] {
                -0.5f, -0.5f,
                -0.5f,  0.5f,
                0.5f, -0.5f,
                0.5f,  0.5f,
                -0.5f,  0.5f,
                0.5f, -0.5f,
        }, 2);
        triangle.uploadBuffer();

        shaderProgram.link();
    }

    @Override
    protected void eachFrame() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        int program = shaderProgram.getCompiledProgram();
        GL20.glUseProgram(program);

        int vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        triangle.enableBuffer();

        GL20.glBindAttribLocation(program, 0, "position");
        GL20.glEnableVertexAttribArray(0);

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);

        GL20.glDisableVertexAttribArray(0);
        GL20.glUseProgram(0);
    }

    public static void main(String[] args) {
        new VoronoiDemo().run();
    }
}
