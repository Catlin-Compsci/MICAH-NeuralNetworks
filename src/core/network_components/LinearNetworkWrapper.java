package core.network_components;

import core.network_components.network_classes.LinearNetwork;

import java.util.ArrayList;

public class LinearNetworkWrapper extends LinearNetwork {
    LinearNetwork innerNetwork;
    ArrayList<Transformer> preTransformers;
    ArrayList<Transformer> postTransformers;


}
