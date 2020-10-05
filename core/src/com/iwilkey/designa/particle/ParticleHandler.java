package com.iwilkey.designa.particle;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.assets.Assets;

import java.util.ArrayList;

public class ParticleHandler {

    public ArrayList<Particle> particles;

    public ParticleHandler(){
        particles = Assets.initParticles();;
    }

    public void startParticle(String name, int x, int y) {
        for(Particle part : particles)
            if(part.name.equals(name)) {
                part.setPosition(x, y);
                part.start();
                return;
            }
    }

    public void startParticle(String name, int x, int y, float scale) {
        for(Particle part : particles)
            if(part.name.equals(name)) {
                part.setPosition(x, y);
                part.setScale(scale);
                part.start();
                return;
            }
    }

    public void tick() {
        for(Particle part : particles) part.update();
    }

    public void render(Batch b) {
        for(Particle part : particles) part.render(b);
    }

}
