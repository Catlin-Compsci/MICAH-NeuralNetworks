package core.network_components.network_classes;

import core.network_components.activation_functions.Linear;

public class SensorNode extends Node {

    public SensorNode() {
        super(new Linear());
    }

    @Override
    public void type() { System.out.println("Sensor"); }

}
