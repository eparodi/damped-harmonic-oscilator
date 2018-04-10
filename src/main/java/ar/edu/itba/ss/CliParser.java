package ar.edu.itba.ss;

import org.apache.commons.cli.*;

public class CliParser {

    static double mass = 70, k = 10000, gamma = 100, tf = 5, r = 1;
    static double speed;

    private static Options createOptions(){
        Options options = new Options();
        options.addOption("h", "help", false, "Shows this screen.");
        options.addOption("m", "mass", true, "Mass of the particle.");
        options.addOption("k", "elasticity", true, "Elasticity of the system.");
        options.addOption("g", "gamma", true, "Gamma of the system.");
        options.addOption("tf", "final-time", true, "Time when the simulation ends");
        options.addOption("r", "position", true, "Initial position of the particle.");
        return options;
    }

    public static void parseOptions(String[] args){
        Options options = createOptions();
        CommandLineParser parser = new BasicParser();

        try {
            CommandLine cmd = parser.parse(options, args);
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

            speed = - gamma / 2 * mass;
        }catch (Exception e){
            System.out.println("Command not recognized.");
            help(options);
        }
    }

    private static void help(Options options){
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("Main", options);
        System.exit(0);
    }
}
