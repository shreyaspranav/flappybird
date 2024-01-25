package org.game.engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class Renderer2D {

    // QUADS_PER_DRAW_CALL represents number of quads drawn per draw call.
    // Changing this impacts performance depending on the GPU
    private static final int MAX_QUADS_PER_DRAW_CALL = 10000;

    // The main vertex and fragment shader path in resources folder that is used to render the quads:
    private static final String VERTEX_SHADER_PATH = "shaders/vertexShader.glsl";
    private static final String FRAGMENT_SHADER_PATH = "shaders/fragmentShader.glsl";

    // Camera View Projection(projection mat * view mat) uniform name:
    private static final String VIEW_PROJECTION_MAT_UNIFORM_NAME = "uViewProjectionMatrix";

    private static float[] defaultQuadVerts = {
             0.5f,  0.5f, 0.0f,
            -0.5f,  0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
             0.5f, -0.5f, 0.0f
    };

    private static int[] defaultQuadIndices = { 0, 1, 2, 0, 2, 3 };


    // Private constants: DO NOT CHANGE
    private static final int FLOATS_PER_VERTEX = 10;

    private static final int SIZE_PER_FLOAT = 4;
    private static final int SIZE_PER_INT = 4;

    private static final int VERTICES_PER_QUAD = 4;
    private static final int INDICES_PER_QUAD = 6;

    private static final int FLOATS_PER_QUAD = FLOATS_PER_VERTEX * VERTICES_PER_QUAD;

    // Arrays that hold the vertex and index data
    private static int quadIndex;
    private static float[] quadVerts;
    private static int[] quadIndices;

    private static Camera2D sceneCamera;
    private static VertexBuffer mainVertexBuffer;
    private static ElementBuffer mainElementBuffer;
    private static ShaderProgram mainShader;

    public static void init() {

        // Setting up the Vertex Buffer and Element Buffer:
        ArrayList<BufferElement> bufferLayout = new ArrayList<>();
        bufferLayout.add(new BufferElement("aPosition", 3, 0));
        bufferLayout.add(new BufferElement("aColor", 4, 1));
        bufferLayout.add(new BufferElement("aTextureCoords", 2, 2));
        bufferLayout.add(new BufferElement("aTextureSlot", 1, 3));

        mainVertexBuffer = new VertexBuffer(bufferLayout, MAX_QUADS_PER_DRAW_CALL * VERTICES_PER_QUAD * FLOATS_PER_VERTEX * SIZE_PER_FLOAT);
        mainElementBuffer = new ElementBuffer(MAX_QUADS_PER_DRAW_CALL * INDICES_PER_QUAD * SIZE_PER_INT);

        mainVertexBuffer.setElementBuffer(mainElementBuffer);

        // Setting up the shader:
        mainShader = new ShaderProgram(VERTEX_SHADER_PATH, FRAGMENT_SHADER_PATH);
        mainShader.loadShader();

        // Setting up the arrays that hold data for the vertex and element buffer:
        quadVerts = new float[MAX_QUADS_PER_DRAW_CALL * VERTICES_PER_QUAD * FLOATS_PER_VERTEX];
        quadIndices = new int[MAX_QUADS_PER_DRAW_CALL * INDICES_PER_QUAD];

//        defaultQuadVerts = new Vector4f[]{
//                new Vector4f(0.5f, 0.5f, 0.0f, 1.0f),
//                new Vector4f(-0.5f, 0.5f, 0.0f, 1.0f),
//                new Vector4f(-0.5f, -0.5f, 0.0f, 1.0f),
//                new Vector4f(0.5f, -0.5f, 0.0f, 1.0f),
//        };
    }

    public static void beginScene(Camera2D camera) {
        quadIndex = 0;
        sceneCamera = camera;
    }

    public static void endScene() {
        flushRenderer();
    }

    public static void drawClearQuad(Vector3f position, float rotation, Vector2f scale, Vector4f color) {
        addQuad(position, rotation, scale);
        addColor(color);

        quadIndex++;
    }

    // Private methods: ------------------------------------------------------------------------------------------------------------------
    private static void addQuad(Vector3f position, float rotation, Vector2f scale) {
        if(quadIndex >= MAX_QUADS_PER_DRAW_CALL) {
            flushRenderer();
            quadIndex = 0;
        }

        // Update the vertices:
        Vector3f[] verts = new Vector3f[4];
        Matrix4f mat = new Matrix4f();
        mat.identity().translate(position).
                rotateZ(0.0f).
                rotateY(0.0f).
                rotateZ((float)Math.toRadians(rotation)).
                scale(new Vector3f(scale, 1.0f));

        for(int i = 0; i < 4; i++) {

            // Perform TRS(Translate, Rotate, Scale) operations:
            verts[i] = new Vector3f(defaultQuadVerts[(i * 3) + 0], defaultQuadVerts[(i * 3) + 1], defaultQuadVerts[(i * 3) + 2]);
            mat.transformPosition(verts[i]);

            // Assign the calculated vertex positions to the allocated array:
            quadVerts[(quadIndex * FLOATS_PER_QUAD) + (i * FLOATS_PER_VERTEX) + 0] = verts[i].x;
            quadVerts[(quadIndex * FLOATS_PER_QUAD) + (i * FLOATS_PER_VERTEX) + 1] = verts[i].y;
            quadVerts[(quadIndex * FLOATS_PER_QUAD) + (i * FLOATS_PER_VERTEX) + 2] = verts[i].z;
        }

        // Update the indices:
        for(int i = 0; i < 6; i++) {
            quadIndices[(quadIndex * INDICES_PER_QUAD) + i] = defaultQuadIndices[i] + quadIndex * 4;
        }
    }

    private static void addColor(Vector4f color) {
        for(int i = 0; i < 4; i++) {
            quadVerts[(quadIndex * FLOATS_PER_QUAD) + (i * FLOATS_PER_VERTEX) + 3] = color.x;
            quadVerts[(quadIndex * FLOATS_PER_QUAD) + (i * FLOATS_PER_VERTEX) + 4] = color.y;
            quadVerts[(quadIndex * FLOATS_PER_QUAD) + (i * FLOATS_PER_VERTEX) + 5] = color.z;
            quadVerts[(quadIndex * FLOATS_PER_QUAD) + (i * FLOATS_PER_VERTEX) + 6] = color.w;
        }
    }

    private static void flushRenderer() {
        // Update the camera:
        sceneCamera.update();

        // Update the Vertex and the Element Buffer by copying the data to the GPU:
        mainVertexBuffer.put(quadVerts);
        mainElementBuffer.put(quadIndices);

        // Bind the shader:
        mainShader.bind();

        // Update the uViewProjection uniform in the shader:
        mainShader.uniformMat4(VIEW_PROJECTION_MAT_UNIFORM_NAME, sceneCamera.getViewProjectionMat());

        // Render out the entire Vertex Buffer:
        RenderCommand.DrawIndexed(mainVertexBuffer, 0, quadIndex * INDICES_PER_QUAD);
    }
}
