package org.game.engine;

import org.lwjgl.system.StructBuffer;

import static org.lwjgl.opengl.GL46.*;

import java.util.ArrayList;
public class VertexBuffer {
    private int vertexBufferHandle;
    private int vertexArrayHandle;

    private int elementBufferHandle;
    private long size;
    private ArrayList<BufferElement> bufferLayout;

    public VertexBuffer(ArrayList<BufferElement> bufferLayout, long size) {
        this.bufferLayout = bufferLayout;
        this.size = size;

        vertexBufferHandle = glCreateBuffers();
        glNamedBufferStorage(vertexBufferHandle, size, GL_DYNAMIC_STORAGE_BIT);

        vertexArrayHandle = glCreateVertexArrays();

        glBindVertexArray(vertexArrayHandle);
        int stride = calculateStride();

        glVertexArrayVertexBuffer(vertexArrayHandle, 0, vertexBufferHandle, 0, stride);

        int offset = 0;
        for (BufferElement element : bufferLayout) {
            glEnableVertexArrayAttrib(vertexArrayHandle, element.shaderLoc);
            glVertexArrayAttribFormat(vertexArrayHandle, element.shaderLoc, element.size, GL_FLOAT, false, offset);
            glVertexArrayAttribBinding(vertexArrayHandle, element.shaderLoc, 0);

            offset += element.size * 4; // Because size of a float variable is 4 bytes
        }
        elementBufferHandle = 0;
    }

    public void setElementBuffer(ElementBuffer elementBuffer) {
        glVertexArrayElementBuffer(vertexArrayHandle, elementBuffer.getElementBufferHandle());
    }

    public void put(float[] data) {
        put(0, data);
    }

    public void put(long offset, float[] data) {
        glNamedBufferSubData(vertexBufferHandle, offset, data);
    }

    public void bind() {
        glBindVertexArray(vertexArrayHandle);
    }

    public void unBind() {
        glBindVertexArray(0);
    }

    private int calculateStride() {
        int stride = 0;
        for (BufferElement element : bufferLayout)
            stride += element.size * 4;  // Because size of a float variable is 4 bytes
        return stride;
    }
}
