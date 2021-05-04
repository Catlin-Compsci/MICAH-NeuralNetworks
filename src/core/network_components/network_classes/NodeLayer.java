package core.network_components.network_classes;

import core.data.ArrayData;
import core.data.ArrayShape;

import java.util.ArrayList;

public class NodeLayer {
    ArrayList<Node> nodes = new ArrayList<>();
//    ErrorFunction errorFunction; //TODO perhaps try out errorfunctions on layers??? idkkkkk

    public NodeLayer(int nodeCount/*, ErrorFunction err*/) {
//        this.errorFunction = err;
        for (int i = 0; i < nodeCount; i++) {
            nodes.add(new BiasedNode());
        }
    }

    protected NodeLayer() {

    }

//    public NodeLayer(int nodeCount) {
//        this(nodeCount,new ErrorSignal());
//    }

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
