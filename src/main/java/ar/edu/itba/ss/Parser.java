package ar.edu.itba.ss;

import ar.edu.itba.ss.voyager.Planet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {

    public static List<Planet> planetParse(String file) throws FileNotFoundException {
        File planetFile = new File(file);
        Scanner sc = new Scanner(planetFile);

        double numberOfPlanets = 3;
        List<Planet> planets = new ArrayList<>();

        for (int i = 0; i < numberOfPlanets; i++) {
            double x = sc.nextDouble() * 1000;
            double y = sc.nextDouble() * 1000;
            double vx = sc.nextDouble() * 1000;
            double vy = sc.nextDouble() * 1000;
            Planet p = new Planet(i+1, x, y, vx, vy);
            planets.add(p);
        }

        sc.close();

        return planets;
    }
}
