package org.game.engine;

import org.game.engine.ext.PNGDecoder;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL46.*;

public class Texture {

    private int textureHandle;
    private final int textureWidth, textureHeight;

    public Texture(String path, boolean usePointFiltering) {
        try {
            PNGDecoder decoder = new PNGDecoder(Texture.class.getClassLoader().getResourceAsStream(path));

            textureWidth = decoder.getWidth();
            textureHeight = decoder.getHeight();

            ByteBuffer buf = ByteBuffer.allocateDirect(
                    4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            buf.flip();

            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

            textureHandle = glCreateTextures(GL_TEXTURE_2D);
            glTextureStorage2D(textureHandle, 1, GL_RGBA8, textureWidth, textureHeight);

            glTextureParameteri(textureHandle, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTextureParameteri(textureHandle, GL_TEXTURE_WRAP_T, GL_REPEAT);
            glTextureParameteri(textureHandle, GL_TEXTURE_MIN_FILTER, usePointFiltering ? GL_NEAREST : GL_LINEAR);
            glTextureParameteri(textureHandle, GL_TEXTURE_MAG_FILTER, usePointFiltering ? GL_NEAREST : GL_LINEAR);

            glTextureSubImage2D(textureHandle, 0, 0, 0, textureWidth, textureHeight, GL_RGBA, GL_UNSIGNED_BYTE, buf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Texture(byte[] pixelData, int width, int height) {
        this.textureWidth = width;
        this.textureHeight = height;

        ByteBuffer buf = ByteBuffer.wrap(pixelData);
        //buf.flip();

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        textureHandle = glCreateTextures(GL_TEXTURE_2D);
        glTextureStorage2D(textureHandle, 1, GL_RGBA8, textureWidth, textureHeight);

        glTextureParameteri(textureHandle, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTextureParameteri(textureHandle, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTextureParameteri(textureHandle, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTextureParameteri(textureHandle, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTextureSubImage2D(textureHandle, 0, 0, 0, textureWidth, textureHeight, GL_RGBA, GL_UNSIGNED_BYTE, ByteBuffer.wrap(pixelData).flip());
    }

    public void bind(int slot) {
        glBindTextureUnit(slot, textureHandle);
    }

    public int getTextureWidth() {
        return textureWidth;
    }

    public int getTextureHeight() {
        return textureHeight;
    }
}
