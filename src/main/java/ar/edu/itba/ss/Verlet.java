package ar.edu.itba.ss;

public class Verlet implements Algorithm{

    private Configuration config;

    public Verlet(Configuration config){
        this.config = config;
    }

    public void run(){
        double previousPosition, position, speed;
        previousPosition = getPreviousPosition();
        position = config.position;
        speed = config.speed;
        double time = 0;
        while (time < config.finalTime){
            double force = - (config.elasticity * position + config.gamma * speed);
            double newPosition = 2 * position - previousPosition + Math.pow(config.deltaTime, 2) * force / config.mass;
            speed = getSpeed(newPosition, previousPosition);
            System.out.println(time + "\t" + position);
            previousPosition = position;
            position = newPosition;
            time += config.deltaTime;
        }
    }

    private double getPreviousPosition(){
        double position = config.position - config.deltaTime * config.speed;
        double force = - (config.elasticity * config.position + config.gamma * config.speed);
        position -= Math.pow(config.deltaTime, 2) * force / (2 * config.mass);
        return position;
    }

    private double getSpeed(double position, double previousPosition){
        return (position - previousPosition) / (2 * config.deltaTime);
    }
}
