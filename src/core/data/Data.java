package core.data;

import core.network_components.network_classes.NodeLayer;

public interface Data<S extends DataShape> {
    public NodeLayer getAsNodeLayer();
    public static <E extends Data> E getFromNodeLayer(NodeLayer nodeLayer) {
        return null;
    }
    public S getShape();
}