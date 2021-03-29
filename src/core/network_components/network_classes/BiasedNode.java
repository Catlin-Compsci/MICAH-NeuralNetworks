package core.network_components.network_classes;

public class BiasedNode extends Node{
    public BiasedNode() {
        this(0);
    }

    public BiasedNode(double data) {
        super(data);
        new Connection(new BiasNode(),this);
    }
}
