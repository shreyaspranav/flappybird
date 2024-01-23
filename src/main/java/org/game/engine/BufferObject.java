package org.game.engine;

import static org.lwjgl.opengl.GL46.*;
public class BufferObject {
    private int bufferID;
    private BufferAttributeType bufferAttributeType;

    public BufferObject(BufferAttributeType type) {
        this.bufferAttributeType = type;

        bufferID = glCreateBuffers();

    }
}
