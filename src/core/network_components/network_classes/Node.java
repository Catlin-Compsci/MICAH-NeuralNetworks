package core.network_components.network_classes;

import core.network_components.activation_functions.ActivationFunction;
import core.network_components.activation_functions.Logistic;
import core.network_components.network_abstract.Emitter;
import core.network_components.network_abstract.Reciever;
import utils.NumUtils;

import java.util.ArrayList;
import java.util.Random;

public class Node implements Emitter, Reciever {

    private double total;
//    public double biasWeight;
    double errorSignal;

    ArrayList<Connection> connectionsIn = new ArrayList<>();
    ArrayList<Connection> connectionsOut = new ArrayList<>();
    ActivationFunction activation = new Logistic();

    //TODO Try changing initial from 0 to 1
    public Node(ActivationFunction activation) {
        this(0,activation);
    }
    public Node(double data, ActivationFunction activation) {
        total = data;
        this.activation = activation;
    }
    public Node(double data) {
        this(data,new Logistic());
    }
    public Node() {
        this(new Logistic());
    }

    public void type() {
        System.out.println("Normal");
    }

    public void pass() {
        connectionsOut.forEach(Connection::run);
    }
    public void receive() {
        reset();
        connectionsIn.forEach(Connection::run);
    }

    @Override
    public double emit() {
        //REMOVED SO THAT BIAS IS A CONNECTION IN
//        return activation.getScaled(total + biasWeight * 1);

        //CACHE THIS VALUE!
        return activation.getScaled(total);
    }

    public void reset() {
        total = 0;
    }

    public void set(double value) {
        total = value;
    }

    @Override
    public void intake(double value) {
        total += value;
    }

    public double getTotal() {
        return total;
    }
}
