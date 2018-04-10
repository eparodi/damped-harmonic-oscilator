package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.List;

public class GearPredictor implements Algorithm{

    private Configuration config;
    private static final int ORDER = 5;
    private static final int LENGTH = 6;
    private static final int[] factorials = {1, 1, 2, 6, 24, 120};
    private static final double[] alphaOrder5 = {3.0/16, 251.0/360, 1, 11.0/18, 1.0/6, 1.0/60};

    public GearPredictor(Configuration config){
        this.config = config;
    }

    public void run(){
        List<Double> derivatives = new ArrayList<>(5);
        derivatives.add(config.position);
        derivatives.add(config.speed);
        derivatives = recursiveDerivatives(ORDER, 1, derivatives);
        double time = 0;
        while(time < config.finalTime){
            double[] predictions = getPrediction(derivatives);
            double error = evaluate(predictions);
            derivatives = fix(predictions, error);
            System.out.println(derivatives);
            time += config.deltaTime;
        }
    }

    private List<Double> recursiveDerivatives(int order, int current, List<Double> values){
        if (current == order){
            return values;
        }
        int size = values.size();
        double value = (config.elasticity * values.get(size - 2) + config.gamma * values.get(size - 1));
        value = - value / config.mass;
        values.add(value);
        return recursiveDerivatives(order, current + 1, values);
    }

    private double[] getPrediction(List<Double> values){
        int size = values.size();
        double[] predictions = new double[LENGTH];
        for (int i = 0; i < size; i++){
            for (int j = 0; j <= i; j++){
                predictions[size - i - 1] += values.get(size - i + j - 1) * Math.pow(config.deltaTime, j) / factorials[j];
            }
        }
        return predictions;
    }

    private double evaluate(double[] predictions){
        double realAcceleration = config.elasticity * predictions[0] + config.gamma * predictions[1];
        realAcceleration = - realAcceleration / config.mass;
        double error = realAcceleration - predictions[2];
        error = error * Math.pow(config.deltaTime, 2) / factorials[2];
        return error;
    }

    private List<Double> fix(double[] predictions, double error){
        List<Double> list = new ArrayList<>(LENGTH);
        for (int i = 0; i < predictions.length; i++){
            double value = predictions[i] + alphaOrder5[i] * error * factorials[i] / Math.pow(config.deltaTime, i);
            list.add(value);
        }
        return list;
    }

}
