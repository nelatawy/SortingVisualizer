package Visualization;

import javax.sound.sampled.*;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TonePlayer {

    private static TonePlayer instance;

    private final SourceDataLine line;

    private final float sampleRate = 44100;

    private final BlockingQueue<Tone> queue = new LinkedBlockingQueue<>();

    private long lastToneTime = 0;

    private TonePlayer() throws LineUnavailableException {

        AudioFormat format = new AudioFormat(sampleRate, 8, 1, true, false);
        line = AudioSystem.getSourceDataLine(format);
        line.open(format);
        line.start();

        startAudioThread();
    }

    public static TonePlayer getInstance() throws LineUnavailableException {
        if (instance == null) {
            instance = new TonePlayer();
        }
        return instance;
    }

    public void playTone(int hz, int durationMs) {
        long now = System.currentTimeMillis();
        if (now - lastToneTime < 50){
            return;
        }
        lastToneTime = now;
        queue.offer(new Tone(hz, durationMs));
    }

    private void startAudioThread() {

        Thread audioThread = new Thread(() -> {
            try {
                while (true) {
                    Tone tone = queue.take();
                    playToneInternal(tone.hz, tone.durationMs);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });

        audioThread.setDaemon(true);
        audioThread.start();
    }

    private void playToneInternal(int hz, int durationMs) {

        int samples = (int) (durationMs * sampleRate / 1000);
        byte[] buffer = new byte[samples];

        for (int i = 0; i < samples; i++) {

            double angle = 2.0 * Math.PI * hz * i / sampleRate;
            buffer[i] = (byte) (Math.sin(angle) * 127);

        }

        line.write(buffer, 0, buffer.length);
    }

    private static class Tone {
        int hz;
        int durationMs;

        Tone(int hz, int durationMs) {
            this.hz = hz;
            this.durationMs = durationMs;
        }
    }
}