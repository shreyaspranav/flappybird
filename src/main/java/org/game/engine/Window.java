package org.game.engine;

import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.game.application.ApplicationProperties;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

public class Window {
    private ApplicationProperties properties;
    private long windowHandle;
    public Window(ApplicationProperties properties) { this.properties = properties; }

    public void createWindow() {
        if(!GLFW.glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        windowHandle = GLFW.glfwCreateWindow(properties.getWindowWidth(),
                properties.getWindowHeight(),
                properties.getWindowTitle(),
                properties.isFullscreen() ? glfwGetPrimaryMonitor() : NULL,
                NULL);

        try (MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            glfwGetWindowSize(windowHandle, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            // Center the window
            GLFW.glfwSetWindowPos(
                    windowHandle,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwSwapInterval(properties.isVsync() ? 1 : 0);
        glfwShowWindow(windowHandle);

        // Create OpenGL Context
        glfwMakeContextCurrent(windowHandle);
        GL.createCapabilities();
    }

    public void updateWindow() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }

    public void destroyWindow() {
        glfwFreeCallbacks(windowHandle);

        glfwDestroyWindow(windowHandle);
        glfwTerminate();
    }

    public boolean isWindowClosing() { return glfwWindowShouldClose(windowHandle); }
}
