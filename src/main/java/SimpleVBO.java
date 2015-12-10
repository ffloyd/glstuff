public interface SimpleVBO {
    void upload(); // upload data to GPU
    void upload(int usage);

    void bind(int attributeIndex);
    void unbind();

    int get(); // get buffer reference
    int getElementsCount();
}
