package org.game.engine;

public class BufferElement {
    public String attributeName;
    public int size;
    public int shaderLoc;

    public BufferElement(String attributeName, int size, int shaderLoc) {
        this.attributeName = attributeName;
        this.size = size;
        this.shaderLoc = shaderLoc;
    }
}
