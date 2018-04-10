package ar.edu.itba.ss;

public class Beeman implements Algorithm {

    private Configuration config;

    public Beeman(Configuration config){
        this.config = config;
    }

    @Override
    public void run() {
        double r = config.position;
        double v = config.speed;

        double dt = config.deltaTime;
        double prevR = r - v * dt; /* x(t - dt) */
        double prevA = acceleration(prevR, v); /* a(t - dt) */

        System.out.println(r);

        for (double t = 0; t < config.finalTime; t+=dt){

            /* Backup values */
            double oldR = r;
            double oldV = v;

            /* a(t) */
            double a = acceleration(r, v);

            r = r + v * dt + (2/3) * a * Math.pow(dt, 2) - (1/6) * prevA * Math.pow(dt, 2);
            double predictedV = v + (3/2) * a * dt - (1/2) * prevA * dt;
            double newA = acceleration(r, predictedV);
            v = v + 1/3 * newA * dt + (5/6) * a * dt - (1/6) * prevA * dt;

            prevA = acceleration(oldR, oldV);

            System.out.println(r);
        }
    }

    private double acceleration(double r, double v){

        double f = -config.elasticity * r - config.gamma * v;

        return f / config.mass;
    }
}
