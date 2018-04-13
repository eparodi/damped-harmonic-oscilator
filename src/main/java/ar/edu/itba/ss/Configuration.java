package ar.edu.itba.ss;

public class Configuration {

    double elasticity, mass, gamma, finalTime, position, speed, deltaTime;

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

}
