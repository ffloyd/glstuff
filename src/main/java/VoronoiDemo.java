public class VoronoiDemo extends GLFWApplication {
    public VoronoiDemo() {
        super("Voronoi Diagrams Demo", 1024, 768);
        setKeyCallback(new BasicKeyCallback());
    }

    @Override
    protected void eachFrame() {
    }

    @Override
    protected void beforeLoop() {
    }

    public static void main(String[] args) {
        new VoronoiDemo().run();
    }
}
