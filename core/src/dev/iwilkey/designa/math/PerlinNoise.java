package dev.iwilkey.designa.math;

public class PerlinNoise {

    long seed;

    public PerlinNoise(long seed) {
        this.seed = seed;
    }

    // lets say seed = 10289473
    private int random(int x, int range) {
        return (int)((x + seed)^5) % range;
    }

    // Perlin noise attempts to smooth random points that are separated by some sample distance sd.
    float noise,
        prog,
        left_random,
        right_random;
    int sampleIndex;

    public int getNoise(int x, int range, int sd) {

        noise = 0;
        range /= 2;

        while(sd > 0) {
            sampleIndex = x / sd;
            prog = (x % sd) / (float)sd;
            left_random = random(sampleIndex, range);
            right_random = random(sampleIndex + 1, range);
            noise += (1 - prog) * left_random + prog * right_random;

            sd /= 2; range /= 2;
            range = Math.max(1, range);
        }

        return Math.round(noise);

    }

}
