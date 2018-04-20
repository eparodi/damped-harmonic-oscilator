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
            double x = sc.nextDouble();
            double y = sc.nextDouble();
            double vx = sc.nextDouble();
            double vy = sc.nextDouble();
            Planet p = new Planet(i+1, x, y, vx, vy);
            planets.add(p);
        }

        return planets;
    }
}
