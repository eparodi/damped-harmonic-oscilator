package ar.edu.itba.ss.voyager;

import ar.edu.itba.ss.Configuration;

import java.util.ArrayList;
import java.util.List;

public class Voyager {

    private static double G = 6.693*Math.pow(10, -11);
    private static double AU = 149598073;
    private static double VOYAGER_DISTANCE = 1500000; /* 1500 km */
    private static double VOYAGER_SPEED = 11000; /* 11 km/s */

    //private static double EARTH_X = -2.75312*Math.pow(10, 10);
    //private static double EARTH_Y = 1.444972*Math.pow(10, 10);
    private static double EARTH_X = -1.496*Math.pow(10, 11);
    private static double EARTH_Y = -1.496*Math.pow(10, 11);

    private static double EARTH_VX = -2.97390*Math.pow(10, 4);
    private static double EARTH_VY = -5000.69217;

    private static final double EARTH_MASS = 5.97219 * Math.pow(10, 24);
    private static final double VOYAGER_MASS = 721;
    private static final double SUN_MASS = 1.988544 * Math.pow(10, 30);
    private static final double JUPITER_MASS = 1898.13 * Math.pow(10, 24);
    private static final double SATURN_MASS = 5.68319 * Math.pow(10, 26);


    public static void voyager(Configuration config) throws CloneNotSupportedException {

        List<Planet> planets = new ArrayList<>();

        double earthSunAngle = 0;
        if (EARTH_X == 0) {
            earthSunAngle = Math.signum(EARTH_Y) * Math.PI / 2;
        }else{
            earthSunAngle = Math.atan(EARTH_Y/EARTH_X);
            if ((EARTH_X < 0 && EARTH_Y > 0) || (EARTH_X < 0 && EARTH_Y < 0)){
                earthSunAngle += Math.PI;
            }
        }

        double tangentAngle = Math.PI - earthSunAngle;

        planets.add(new Planet(1, EARTH_X + VOYAGER_DISTANCE*Math.cos(earthSunAngle), EARTH_Y + VOYAGER_DISTANCE*Math.sin(earthSunAngle), EARTH_VX + VOYAGER_SPEED*Math.cos(tangentAngle), EARTH_VY + VOYAGER_SPEED*Math.sin(tangentAngle), VOYAGER_MASS, 0.002)); //Voyager 1
        planets.add(new Planet(2, 0.0, 0.0, 0, 0, SUN_MASS, 0.04)); // Sun
        planets.add(new Planet(3, EARTH_X, EARTH_Y, EARTH_VX, EARTH_VY, EARTH_MASS, 0.03)); // Earth
        planets.add(new Planet(4, -20000000.0, 100000.0, 100, 100, JUPITER_MASS, 0.03)); // Jupiter
        planets.add(new Planet(5, 100000.0, 200000.0, 100, 100, SATURN_MASS, 0.03)); // Saturn

        double dt = config.deltaTime;

        /* Initialize variables */
        for (Planet p : planets){
            if (p.id != 2) {
                p.prevX = p.x - p.vx * dt; /* x(t - dt) */
                p.prevY = p.y - p.vy * dt;
                //p.prevX = p.x - VOYAGER_DISTANCE*Math.cos((VOYAGER_SPEED/VOYAGER_DISTANCE)*dt);
                //p.prevY = p.y - VOYAGER_DISTANCE*Math.sin((VOYAGER_SPEED/VOYAGER_DISTANCE)*dt);

                double[] force = force(p, planets);
                p.prevAx = force[0];
                p.prevAy = force[1];
            }
        }

        int iterations = 0;
        System.out.println(planets.size());
        System.out.println(iterations++);
        printPlanets(planets);

        List<Planet> oldPlanets = new ArrayList<>();

        for (double t = 0; t < config.finalTime; t+=dt) {

            for (Planet p: planets){
                oldPlanets.add(p.getClone());
            }

            for (Planet p : planets) {
                if (p.id != 2) {
                    double force[] = force(p, oldPlanets);

                    double fx = force[0];
                    double fy = force[1];


                    /* Beeman */
                    p.x = p.x + p.vx * dt + (2.0 / 3) * fx * Math.pow(dt, 2) - (1.0 / 6) * p.prevAx * Math.pow(dt, 2);
                    p.y = p.y + p.vy * dt + (2.0 / 3) * fy * Math.pow(dt, 2) - (1.0 / 6) * p.prevAy * Math.pow(dt, 2);

                    double[] newForce = force(p, oldPlanets);

                    double newAx = newForce[0];
                    double newAy = newForce[1];

                    p.vx = p.vx + 1.0 / 3 * newAx * dt + (5.0 / 6) * fx * dt - (1.0 / 6) * p.prevAx * dt;
                    p.vy = p.vy + 1.0 / 3 * newAy * dt + (5.0 / 6) * fy * dt - (1.0 / 6) * p.prevAy * dt;

                    p.prevAx = fx;
                    p.prevAy = fy;
                }
            }
            oldPlanets = new ArrayList<>();
            System.out.println(planets.size());
            System.out.println(iterations++);
            printPlanets(planets);
        }
    }

    private static void printPlanets(List<Planet> planets) {
        for (Planet pi : planets){
            double aux  = pi.x/1000;
            aux = aux/AU;
            double auy = pi.y/1000;
            auy = auy/AU;
            System.out.println(aux + "\t" + auy + "\t" + pi.vx + "\t" + pi.vy + "\t" + pi.radius);
        }
    }

    private static double gravitationalForce(Planet pi, Planet pj){

        double distance = Math.sqrt(Math.pow(pi.x - pj.x, 2) + Math.pow(pi.y - pj.y, 2));

        return G*(pi.mass*pj.mass/Math.pow(distance, 2));
    }

    private static double[] force (Planet p, List<Planet> oldPlanets){
        double[] force = {0,0};
        for (Planet otherPlanet : oldPlanets) {
            if (p.id != otherPlanet.id) {

                double f = gravitationalForce(p, otherPlanet);

                double dx = otherPlanet.x - p.x;
                double dy = otherPlanet.y - p.y;
                double angle = Math.atan(dy / dx);
                force[0] += f * Math.cos(angle);
                force[1] += f * Math.sin(angle);
            }
        }
        force[0] = force[0]/p.mass;
        force[1] = force[1]/p.mass;
        return force;
    }
}
