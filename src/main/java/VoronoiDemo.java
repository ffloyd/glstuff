import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class VoronoiDemo extends GLFWApplication {

    private SimpleFloatVBO triangle;

    private Shader flatVert;
    private Shader simpleFrag;

    public VoronoiDemo() {
        super("Voronoi Diagrams Demo", 1024, 768);
        setKeyCallback(new BasicKeyCallback());

        flatVert    = new Shader("flat.vert", GL20.GL_VERTEX_SHADER);
        simpleFrag  = new Shader("simple.frag", GL20.GL_FRAGMENT_SHADER);
    }

    @Override
    protected void beforeLoop() {
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);


        triangle = new SimpleFloatVBO(new float[] {
                -1.0f,  -1.0f,  0.0f,
                1.0f,   -1.0f,  0.0f,
                0.0f,   1.0f,   0.0f,
        });
        triangle.uploadBuffer();

        flatVert.compile();
        simpleFrag.compile();
    }

    @Override
    protected void eachFrame() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GL20.glEnableVertexAttribArray(0);

        triangle.enableBuffer();
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);

        GL20.glDisableVertexAttribArray(0);
    }

    public static void main(String[] args) {
        new VoronoiDemo().run();
    }
}
