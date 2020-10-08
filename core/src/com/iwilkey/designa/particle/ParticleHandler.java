package com.iwilkey.designa.particle;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.assets.Assets;

import java.util.ArrayList;

public class ParticleHandler {

    public ArrayList<Particle> particles;
    public ArrayList<Particle> runningParticles;

    public ParticleHandler(){
        particles = Assets.initParticles();
        runningParticles = new ArrayList<>();
    }

    public void startParticle(String name, int x, int y) {
        for(Particle part : particles)
            if(part.name.equals(name)) {
                part.setPosition(x, y);
                part.start();
                runningParticles.add(part);
                return;
            }
    }

    public void tick() {
        for(Particle part : runningParticles) {
            part.update();
            if(part.isDone()) {
                runningParticles.remove(part);
                break;
            }
        }
    }

    public void render(Batch b) {
        for(Particle part : runningParticles) part.render(b);
    }

}
