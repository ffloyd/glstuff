public interface SimpleUBO<InputType> {
    void build(int bindingPoint);
    void update(InputType data);
    int getBindingPoint();
}
