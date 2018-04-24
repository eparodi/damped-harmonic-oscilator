package ar.edu.itba.ss.voyager;

import ar.edu.itba.ss.Configuration;

import java.util.ArrayList;
import java.util.List;

public class Voyager {

    private static double G = 6.693*Math.pow(10, -11);
    private static final double AU = 149598073;
    private static final double VOYAGER_DISTANCE = 1500000; /* 1500 km */
    private static final double VOYAGER_SPEED = 14000; /* 11 km/s */

    private static final int SUN_ID = 0;

    private static final double EARTH_MASS = 5.97219 * Math.pow(10, 24);
    private static final double VOYAGER_MASS = 721;
    private static final double SUN_MASS = 1.988544 * Math.pow(10, 30);
    private static final double JUPITER_MASS = 1898.13 * Math.pow(10, 24);
    private static final double SATURN_MASS = 5.68319 * Math.pow(10, 26);

    private static final double EARTH_RADIUS = 6371000;
    private static final int[] EARTH_COLOUR = {0, 0, 255};
    private static final int[] VOYAGER_COLOUR = {255, 255, 255};
    private static final int[] SUN_COLOUR = {255, 255, 0};
    private static final int[] JUPITER_COLOUR = {255, 165, 0};
    private static final int[] SATURN_COLOUR = {0, 255, 0};

    public static void voyager(Configuration config, List<Planet> planets) throws CloneNotSupportedException {

        Planet earth = planets.get(0);

        double earthSunAngle;
        if (earth.x == 0) {
            earthSunAngle = Math.signum(earth.y) * Math.PI / 2;
        }else{
            earthSunAngle = Math.atan(earth.y/earth.x);
            if ((earth.x < 0 && earth.y > 0) || (earth.x < 0 && earth.y < 0)){
                earthSunAngle += Math.PI;
            }
        }

        double velocityAngle;
        if (earth.vx == 0) {
            velocityAngle = Math.signum(earth.vy) * Math.PI / 2;
        }else{
            velocityAngle = Math.atan(earth.vy/earth.vx);
            if ((earth.vx < 0 && earth.vy > 0) || (earth.vx < 0 && earth.vy < 0)){
                velocityAngle += Math.PI;
            }
        }

        double voyagerX = earth.x + (VOYAGER_DISTANCE + EARTH_RADIUS) *Math.cos(earthSunAngle);
        double voyagerY = earth.y + (VOYAGER_DISTANCE + EARTH_RADIUS) *Math.sin(earthSunAngle);
        double voyagerVx = earth.vx + VOYAGER_SPEED*Math.cos(velocityAngle);
        double voyagerVy = earth.vy + VOYAGER_SPEED*Math.sin(velocityAngle);

        earth.mass = EARTH_MASS;
        earth.radius = 0.13;
        earth.colour = EARTH_COLOUR;

        Planet jupiter = planets.get(1);
        jupiter.mass = JUPITER_MASS;
        jupiter.radius = 0.13;
        jupiter.colour = JUPITER_COLOUR;

        Planet saturn = planets.get(2);
        saturn.mass = SATURN_MASS;
        saturn.radius = 0.13;
        saturn.colour = SATURN_COLOUR;

        planets.add(new Planet(SUN_ID, 0.0, 0.0, 0, 0, SUN_MASS, 0.14, SUN_COLOUR)); // Sun
        planets.add(new Planet(4, voyagerX, voyagerY, voyagerVx, voyagerVy, VOYAGER_MASS, 0.08, VOYAGER_COLOUR)); //Voyager 1

        double dt = config.deltaTime;

        List<Planet> oldPlanets = new ArrayList<>();

        for (Planet p: planets){
            oldPlanets.add(p.getClone());
        }

        /* Initialize variables */
        for (Planet p : planets){
            if (p.id != SUN_ID) {
                firstStep(p, oldPlanets, dt);
            }
        }

        int iterations = 0;
        System.out.println(planets.size());
        System.out.println(iterations++);
        printPlanets(planets);
        oldPlanets = new ArrayList<>();

        int dt2 = 0;

        for (double t = 0; t < config.finalTime; t+=dt) {

            for (Planet p: planets){
                oldPlanets.add(p.getClone());
            }

            for (Planet p : planets) {
                if (p.id != SUN_ID) {
                    double force[] = force(p, oldPlanets);

                    p.ax = force[0];
                    p.ay = force[1];


                    /* Beeman */
                    p.x = p.x + p.vx * dt + (2.0 / 3) * p.ax * Math.pow(dt, 2) - (1.0 / 6) * p.prevAx * Math.pow(dt, 2);
                    p.y = p.y + p.vy * dt + (2.0 / 3) * p.ax * Math.pow(dt, 2) - (1.0 / 6) * p.prevAy * Math.pow(dt, 2);
                }
            }

            for (Planet p : planets) {
                if (p.id != SUN_ID) {

                    double[] newForce = force(p, planets);

                    double newAx = newForce[0];
                    double newAy = newForce[1];

                    p.vx = p.vx + (1.0 / 3) * newAx * dt + (5.0 / 6) * p.ax * dt - (1.0 / 6) * p.prevAx * dt;
                    p.vy = p.vy + (1.0 / 3) * newAy * dt + (5.0 / 6) * p.ay * dt - (1.0 / 6) * p.prevAy * dt;

                    p.prevAx = p.ax;
                    p.prevAy = p.ay;
                }
            }

            oldPlanets = new ArrayList<>();
            if (dt2++ % config.fps == 0) {
                System.out.println(planets.size());
                System.out.println(iterations++);
                printPlanets(planets);
            }
        }
    }

    private static void printPlanets(List<Planet> planets) {
        for (Planet pi : planets){
            double aux  = pi.x/1000;
            aux = aux/AU;
            double auy = pi.y/1000;
            auy = auy/AU;
            System.out.println(aux + "\t" + auy + "\t" + pi.vx + "\t" + pi.vy + "\t" + pi.radius +
                "\t" + pi.colour[0] + "\t" + pi.colour[1] + "\t" + pi.colour[2]);
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

                double mod = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy,2));
                force[0] += f * (dx/mod);
                force[1] += f * (dy/mod);
            }
        }
        force[0] = force[0]/p.mass;
        force[1] = force[1]/p.mass;
        return force;
    }

    private static void firstStep(Planet p, List<Planet> planets, double dt){
        double[] force = force(p, planets);
        p.prevAx = force[0];
        p.prevAy = force[1];

        p.vx = p.vx + dt * p.prevAx;
        p.vy = p.vy + dt * p.prevAy;
        p.x = p.x + dt * p.vx + Math.pow(dt, 2) * p.prevAx;
        p.y = p.y + dt * p.vy + Math.pow(dt, 2) * p.prevAy;
    }

}
