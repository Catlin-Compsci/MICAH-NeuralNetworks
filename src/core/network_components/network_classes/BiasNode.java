package core.network_components.network_classes;

import core.network_components.activation_functions.Linear;
import core.network_components.activation_functions.Logistic;
import utils.NumUtils;

public class BiasNode extends Node{

    public BiasNode() {
        super(1,new Linear());
    }

    @Override
    public double emit() {
//        System.out.println("BiasNode weight: " + connectionsOut.get(0).weight);
        return super.emit();
    }

    @Override
    public void intake(double value) {
        System.out.println("Oops! You put a bias node into a network, silly you!");
    }

    @Override
    public void type() {
        System.out.println("Bias Node");
    }
}
