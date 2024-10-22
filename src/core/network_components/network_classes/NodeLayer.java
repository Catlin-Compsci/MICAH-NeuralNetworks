package core.network_components.network_classes;

import core.data.ArrayData;
import core.data.ArrayShape;
import core.network_components.activation_functions.ActivationFunction;
import core.network_components.activation_functions.Logistic;
import core.network_components.validation_functions.ValidationFunction;

import java.util.ArrayList;

public class NodeLayer {
    ArrayList<Node> nodes = new ArrayList<>();
//    ErrorFunction errorFunction; //TODO perhaps try out errorfunctions on layers??? idkkkkk

    public NodeLayer(int nodeCount, ActivationFunction activationFunction) {
//        this.errorFunction = err;
        for (int i = 0; i < nodeCount; i++) {
            nodes.add(new BiasedNode(activationFunction));
        }
    }

    public NodeLayer(int nodeCount) {
        this(nodeCount,new Logistic());
    }


        // Default constructor for SensorLayer
    protected NodeLayer() {}

    public ArrayShape getShape() {
        return new ArrayShape(nodes.size());
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void pass() {
        nodes.forEach(Node::pass);
    }

    public void recieve() {
        nodes.forEach(Node::receive);
    }

    public ArrayData getAsArray() {
        ArrayData ret = new ArrayData();
        nodes.forEach(n->ret.add(n.emit()));
        return ret;
    }

}
