package org.game.engine;

import static org.lwjgl.opengl.GL45.*;

public class ElementBuffer {

    private int elementBufferHandle;

    public ElementBuffer(int size) {
        elementBufferHandle = glCreateBuffers();
        glNamedBufferStorage(elementBufferHandle, size, GL_DYNAMIC_STORAGE_BIT);
    }

    public void put(int[] data) {
        put(0, data);
    }

    public void put(long offset, int[] data) {
        glNamedBufferSubData(elementBufferHandle, offset, data);
    }

    public int getElementBufferHandle() {
        return elementBufferHandle;
    }

}
