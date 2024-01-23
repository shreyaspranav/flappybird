package org.game.engine;

import static org.lwjgl.opengl.GL46.*;

public class RenderCommand {

    public static void ClearScreen(float r, float g, float b, float a) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
        glClearColor(r, g, b, a);
    }
    public static void DrawIndexed(VertexBuffer buffer, int start, int count) {
        buffer.bind();
        glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, start);

        //System.out.println(glGetError());
    }
    public static void DrawNonIndexed(VertexBuffer buffer, int start, int count) {
        buffer.bind();
        glDrawArrays(GL_TRIANGLES, start, count);
    }
}
