package io.github.ffloyd.glstuff;

public abstract class BuildableOnce {
    private boolean built;
    private int reference;

    public int build() {
        if (!built) {
            reference   = processBuild();
            built       = true;
            return reference;
        } else {
            throw new RuntimeException("Cannot build resource twice");
        }
    }

    public int getReference() {
        if (built) {
            return reference;
        } else {
            throw new RuntimeException("Resource hasn't built");
        }
    }

    abstract protected int processBuild(); // should return OpenGL reference
}
