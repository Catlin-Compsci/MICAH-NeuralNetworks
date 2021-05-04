package core.network_components.network_classes;

public class SensorLayer extends NodeLayer {
    public SensorLayer(int nodeCount) {
        super();
        for (int i = 0; i < nodeCount; i++) {
            nodes.add(new SensorNode());
        }
    }
}
