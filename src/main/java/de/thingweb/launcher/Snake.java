package de.thingweb.launcher;

import de.thingweb.leddemo.DemoLedAdapter;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Created by Johannes on 07.04.2016.
 */
public class Snake {

    private final DemoLedAdapter np;
    private final Random rnd;
    private Queue<Execution> execs = new LinkedList<>();

    public Snake(DemoLedAdapter np) {
        this.np = np;
        this.rnd = new Random();
    }

    public void start() {
        Execution exe = new Execution();
        exe.stop = false;
        exe.runner = new Thread(exe);
        exe.runner.start();

        execs.add(exe);
    }

    public void stop() {
        Execution exe = execs.poll();
        if (exe != null)
            exe.stop = true;
    }

    public int count() {
        return execs.size();
    }

    private class Execution implements Runnable {
        private Thread runner;
        private boolean stop = false;
        private int active = 0;
        private byte curr;
        private byte curg;
        private byte curb;

        @Override
        public void run() {
            np.setBrightnessPercent((short) 10);
            curr = (byte) rnd.nextInt(255);
            curg = (byte) rnd.nextInt(255);
            curb = (byte) rnd.nextInt(255);
            active = 0;
            while (!stop) {
                if (active == 64) {
                    curr = (byte) rnd.nextInt(255);
                    curg = (byte) rnd.nextInt(255);
                    curb = (byte) rnd.nextInt(255);
                    active = 0;
                }
                np.setColor(active++, curr, curg, curb);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    this.stop = true;
                }
            }
        }
    }
}
