package org.example.control;

public class PlaybackControl {
    private volatile boolean paused = false;
    private volatile boolean stepMode = false;

    public void pause() {
        paused = true;
        stepMode = false;
    }

    public void resume() {
        paused = false;
        stepMode = false;
    }

    public void stepNext() {
        paused = false;
        stepMode = true;
    }


    public void sleepOrPause(int speed) throws InterruptedException {

        while (paused) {
            Thread.sleep(15);
        }
        if (stepMode) {
            paused = true;
        } else {

            Thread.sleep(speed);
        }
    }
}