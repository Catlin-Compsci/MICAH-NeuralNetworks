package core.network_components;

import core.data.ArrayData;
import core.data.ArrayShape;
import core.network_components.network_classes.LinearNetwork;
import core.network_components.network_wrappers.Transformer;

import java.util.ArrayList;

public class LinearNetworkWrapper extends LinearNetwork {
    LinearNetwork innerNetwork;
    ArrayList<Transformer> preTransformers;
    ArrayList<Transformer> postTransformers;

    public LinearNetworkWrapper(ArrayShape inputShape) {
        super(inputShape);
    }

    @Override
    public ArrayData predict(ArrayData input) {
        return super.predict(input);
    }
}
