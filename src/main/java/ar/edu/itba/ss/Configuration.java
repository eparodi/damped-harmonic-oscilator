package ar.edu.itba.ss;

public class Configuration {

    public double elasticity, mass, gamma, finalTime, position, speed, deltaTime, fps;

    public Configuration(double elasticity, double mass, double gamma,
                         double finalTime, double position, double deltaTime){
        this.elasticity = elasticity;
        this.mass = mass;
        this.gamma = gamma;
        this.finalTime = finalTime;
        this.position = position;
        this.speed = - gamma / (2 * mass);
        this.deltaTime = deltaTime;
    }

    public Configuration(double finalTime, double deltaTime, double fps){
        this.finalTime = finalTime;
        this.deltaTime = deltaTime;
        this.fps = fps;
    }

}
