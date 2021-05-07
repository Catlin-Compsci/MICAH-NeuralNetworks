package core.network_components.network_classes;

import core.network_components.activation_functions.ActivationFunction;
import core.network_components.activation_functions.Logistic;

public class BiasedNode extends Node{
    public BiasedNode() {
        this(0);
    }

    public BiasedNode(double data, ActivationFunction activationFunction) {
        super(data, activationFunction);
        new Connection(new BiasNode(),this);
    }

    public BiasedNode(double data) {
        this(data, new Logistic());
    }

    public BiasedNode(ActivationFunction activationFunction) {
        this(0,activationFunction);
    }
}
