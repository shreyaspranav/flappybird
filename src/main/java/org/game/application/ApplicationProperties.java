package org.game.application;

public class ApplicationProperties {
    private int width, height;
    private String title;
    private boolean fullscreen;
    private boolean vsync;

    public ApplicationProperties(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;

        fullscreen = false;
        vsync = true;
    }
    public ApplicationProperties(int width, int height, String title, boolean fullscreen, boolean vsync) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.fullscreen = fullscreen;
        this.vsync = vsync;
    }

    public int getWindowWidth() {
        return width;
    }

    public void setWindowWidth(int width) {
        this.width = width;
    }

    public int getWindowHeight() {
        return height;
    }

    public void setWindowHeight(int height) {
        this.height = height;
    }

    public String getWindowTitle() {
        return title;
    }

    public void setWindowTitle(String title) {
        this.title = title;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public boolean isVsync() {
        return vsync;
    }

    public void setVsync(boolean vsync) {
        this.vsync = vsync;
    }
}
