package ar.edu.itba.ss;

import ar.edu.itba.ss.voyager.Voyager;
import org.apache.commons.cli.*;

public class CliParser {

    private static Options createOptions(){
        Options options = new Options();
        options.addOption("h", "help", false, "Shows this screen.");
        options.addOption("m", "mass", true, "Mass of the particle.");
        options.addOption("k", "elasticity", true, "Elasticity of the system.");
        options.addOption("g", "gamma", true, "Gamma of the system.");
        options.addOption("tf", "finalTime", true, "Time when the simulation ends");
        options.addOption("r", "position", true, "Initial position of the particle.");
        options.addOption("dt", "deltaTime", true, "Interval of time.");
        options.addOption("alg", "algorithm", true, "Algorithm to run.");
        options.addOption("vo", "voyager", false, "Simulate Voyager 1.");
        return options;
    }

    public static Algorithm parseOptions(String[] args){
        Options options = createOptions();
        CommandLineParser parser = new BasicParser();

        double mass = 70, k = 10000, gamma = 100, tf = 5, r = 1, dt = 0.01;
        String algorithm = "gp";
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("h"))
                help(options);
            if (cmd.hasOption("m"))
                mass = Double.parseDouble(cmd.getOptionValue("m"));
            if (cmd.hasOption("k"))
                k = Double.parseDouble(cmd.getOptionValue("k"));
            if (cmd.hasOption("g"))
                gamma = Double.parseDouble(cmd.getOptionValue("g"));
            if (cmd.hasOption("tf"))
                tf = Double.parseDouble(cmd.getOptionValue("tf"));
            if (cmd.hasOption("r"))
                r = Double.parseDouble(cmd.getOptionValue("r"));
            if (cmd.hasOption("dt"))
                dt = Double.parseDouble(cmd.getOptionValue("dt"));
            if (cmd.hasOption("alg"))
                algorithm = cmd.getOptionValue("alg");
            if (cmd.hasOption("vo")) {
                Configuration config = new Configuration(tf, dt);
                Voyager.voyager(config);
                System.exit(0);
            }

            Configuration config = new Configuration(k, mass, gamma, tf, r, dt);
            switch (algorithm){
                case "gp":
                    return new GearPredictor(config);
                case "bm":
                    return new Beeman(config);
                case "ve":
                    return new Verlet(config);
                default:
                    throw new IllegalStateException("No algorithm available");
            }

        }catch (Exception e){
            System.out.println("Command not recognized.");
            help(options);
        }

        return null;
    }

    private static void help(Options options){
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("damped-harmonic-oscilator", options);
        System.exit(0);
    }
}
