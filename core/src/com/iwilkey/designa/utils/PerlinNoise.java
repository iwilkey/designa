package com.iwilkey.designa.utils;

public class PerlinNoise {

    long seed;

    public PerlinNoise(long seed) {
        this.seed = seed;
    }

    private int random(int x, int range) {
        return (int)((x+seed)^5) % range;
    }

    public int getNoise(int x, int range, int sd) {
        float noise = 0;
        range /= 2;

        while(sd > 0) {
            int sampleIndex = x / sd;
            float prog = (x % sd) / (float)(sd);
            float left_random = random(sampleIndex, range);
            float right_random = random(sampleIndex + 1, range);
            noise += (1 - prog) * left_random + prog * right_random;

            sd /= 2;
            range /= 2;
            range = Math.max(1, range);
        }

        return Math.round(noise);
    }

}
