public class VoronoiDemo extends GLFWApplication {
    private GLHelper glHelper;

    public VoronoiDemo() {
        super("Voronoi Diagrams Demo", 1024, 768);
        glHelper = new GLHelper();
        setKeyCallback(new BasicKeyCallback());
    }

    @Override
    protected void beforeLoop() {
        glHelper.init();
    }

    @Override
    protected void eachFrame() {
        glHelper.prepareFrame();
    }

    public static void main(String[] args) {
        new VoronoiDemo().run();
    }
}
