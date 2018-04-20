package ar.edu.itba.ss.voyager;

import ar.edu.itba.ss.Configuration;

import java.util.ArrayList;
import java.util.List;

public class Voyager {

    private static double G = 6.693*Math.pow(10, -11);
    private static final double AU = 149598073;
    private static final double VOYAGER_DISTANCE = 1500000; /* 1500 km */
    private static final double VOYAGER_SPEED = 11000; /* 11 km/s */

    private static final int SUN_ID = 0;

    private static final double EARTH_MASS = 5.97219 * Math.pow(10, 24);
    private static final double VOYAGER_MASS = 721;
    private static final double SUN_MASS = 1.988544 * Math.pow(10, 30);
    private static final double JUPITER_MASS = 1898.13 * Math.pow(10, 24);
    private static final double SATURN_MASS = 5.68319 * Math.pow(10, 26);

    private static final double EARTH_RADIUS = 6371000;

    public static void voyager(Configuration config, List<Planet> planets) throws CloneNotSupportedException {

        Planet earth = planets.get(0);

        double earthSunAngle = 0;
        if (earth.x == 0) {
            earthSunAngle = Math.signum(earth.y) * Math.PI / 2;
        }else{
            earthSunAngle = Math.atan(earth.y/earth.x);
            if ((earth.x < 0 && earth.y > 0) || (earth.x < 0 && earth.y < 0)){
                earthSunAngle += Math.PI;
            }
        }

        double tangentAngle = Math.PI - earthSunAngle;

        earth.mass = EARTH_MASS;
        earth.radius = 0.03;

        Planet jupiter = planets.get(1);
        jupiter.mass = JUPITER_MASS;
        jupiter.radius = 0.03;

        Planet saturn = planets.get(2);
        saturn.mass = SATURN_MASS;
        saturn.radius = 0.03;

        planets.add(new Planet(SUN_ID, 0.0, 0.0, 0, 0, SUN_MASS, 0.04)); // Sun
        planets.add(new Planet(4, EARTH_RADIUS + earth.x + VOYAGER_DISTANCE*Math.cos(earthSunAngle), EARTH_RADIUS + earth.y + VOYAGER_DISTANCE*Math.sin(earthSunAngle), earth.vx + VOYAGER_SPEED*Math.cos(tangentAngle), earth.vy + VOYAGER_SPEED*Math.sin(tangentAngle), VOYAGER_MASS, 0.02)); //Voyager 1

        double dt = config.deltaTime;

        /* Initialize variables */
        for (Planet p : planets){
            if (p.id != SUN_ID) {
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
                if (p.id != SUN_ID) {
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
