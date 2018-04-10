package ar.edu.itba.ss;

import static java.lang.System.exit;

public class App 
{
    public static void main( String[] args ) {

        Algorithm alg = CliParser.parseOptions(args);

        try{
            if (alg == null){
                throw new NullPointerException();
            }
            alg.run();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("There was an error while executing the program, please try again");
            exit(1);
        }

    }}
