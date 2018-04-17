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

    public static void voyager(Configuration config) throws CloneNotSupportedException {

        List<Planet> planets = new ArrayList<>();

        //planets.add(new Planet(1, EARTH_X + VOYAGER_DISTANCE*Math.cos(Math.PI/4), EARTH_Y + VOYAGER_DISTANCE*Math.sin(Math.PI/4), EARTH_VX + VOYAGER_SPEED*Math.cos(3*Math.PI/4), EARTH_VY + VOYAGER_SPEED*Math.sin(3*Math.PI/4), 721, 0.002)); //Voyager 1
        planets.add(new Planet(2, 0.0, 0.0, 0, 0, 1.988544 * Math.pow(10, 30), 0.04)); // Sun
        planets.add(new Planet(3, EARTH_X, EARTH_Y, EARTH_VX, EARTH_VY, 5.97219 * Math.pow(10, 24), 0.03)); // Earth
        //planets.add(new Planet(4, 0.0, 0.0, 0, 0, 1898.13 * Math.pow(10, 24), 4.258*Math.pow(10, -5))); // Jupiter
        //planets.add(new Planet(5, 0.0, 0.0, 0, 0, 5.68319 * Math.pow(10, 26), 4.258*Math.pow(10, -5))); // Saturn

        double dt = config.deltaTime;

        /* Initialize variables */
        for (Planet p : planets){
            if (p.id != 2) {
                p.prevX = p.x - p.vx * dt; /* x(t - dt) */
                p.prevY = p.y - p.vy * dt;
                //p.prevX = p.x - VOYAGER_DISTANCE*Math.cos((VOYAGER_SPEED/VOYAGER_DISTANCE)*dt);
                //p.prevY = p.y - VOYAGER_DISTANCE*Math.sin((VOYAGER_SPEED/VOYAGER_DISTANCE)*dt);

                for (Planet otherPlanet : planets) {
                    if (p.id != otherPlanet.id) {
                        double prevA = acceleration(p, otherPlanet);

                        double dx = otherPlanet.x - p.x;
                        double dy = otherPlanet.y - p.y;
                        double angle = Math.atan(dy/dx);
                        p.prevAx += prevA * Math.cos(angle);
                        p.prevAy += prevA * Math.sin(angle);
                    }
                }
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
                    double fx = 0;
                    double fy = 0;

                    for (Planet otherPlanet : oldPlanets) {
                        if (p.id != otherPlanet.id) {

                            double f = acceleration(p, otherPlanet);

                            double dx = otherPlanet.x - p.x;
                            double dy = otherPlanet.y - p.y;
                            double angle = Math.atan(dy / dx);
                            fx += f * Math.cos(angle);
                            fy += f * Math.sin(angle);
                        }
                    }

                    //fx = fx/p.mass;
                    //fy = fy/p.mass;

                    /* Beeman */
                    p.x = p.x + p.vx * dt + (2.0 / 3) * fx * Math.pow(dt, 2) - (1.0 / 6) * p.prevAx * Math.pow(dt, 2);
                    p.y = p.y + p.vy * dt + (2.0 / 3) * fy * Math.pow(dt, 2) - (1.0 / 6) * p.prevAy * Math.pow(dt, 2);

                    double newAx = 0;
                    double newAy = 0;

                    for (Planet otherPlanet : oldPlanets) {
                        if (p.id != otherPlanet.id) {
                            double newA = 0;
                            newA = acceleration(p, otherPlanet);

                            double dx = otherPlanet.x - p.x;
                            double dy = otherPlanet.y - p.y;
                            double angle = Math.atan(dy / dx);
                            fx += newA * Math.cos(angle);
                            fy += newA * Math.sin(angle);
                        }
                    }

                    //newAx = newAx/p.mass;
                    //newAy = newAy/p.mass;

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

    private static double acceleration (Planet pi, Planet pj){

        double distance = Math.sqrt(Math.pow(pi.x - pj.x, 2) + Math.pow(pi.y - pj.y, 2));

        return G*pi.mass*pj.mass/Math.pow(distance, 2);
    }
}
