package org.game.engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera2D {
    private Vector3f position;
    private float rotation;
    private Vector2f scale;
    private Matrix4f projection, view, viewProjection;

    public Camera2D(Vector3f initialPosition, int width, int height) {
        position = initialPosition;
        rotation = 0.0f;
        scale = new Vector2f(1.0f, 1.0f);

        projection = new Matrix4f();
        view = new Matrix4f();

        float aspectRatio = (float)width / (float)height;
        projection.setOrtho2D(-aspectRatio, aspectRatio, -1.0f, 1.0f);

        view.translate(position.mul(-1.0f, position));
        view.rotate(-rotation, new Vector3f(0.0f, 0.0f, 1.0f));
        view.scale(scale.x, scale.y, 1.0f);

        viewProjection = projection.mul(view);
    }

    public void update() {
        view.translate(position.mul(-1.0f, position));
        view.rotate(-rotation, new Vector3f(0.0f, 0.0f, 1.0f));
        view.scale(scale.x, scale.y, 1.0f);

        viewProjection = projection.mul(view);
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setScale(Vector2f scale) {
        this.scale = scale;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getRotation() {
        return rotation;
    }

    public Vector2f getScale() {
        return scale;
    }

    public Matrix4f getViewProjectionMat() {
        return viewProjection;
    }
}
