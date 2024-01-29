package org.game.engine;

import static org.lwjgl.glfw.GLFW.*;

public class Input {

    private static Window window;

    public static void setWindow(Window w) { window = w; }

    public static boolean isKeyPressed(int key) {
        return glfwGetKey(window.getWindowHandle(), key) == GLFW_PRESS;
    }
}
