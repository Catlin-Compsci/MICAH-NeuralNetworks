package core.network_components.network_classes;

import core.data.ArrayData;
import core.data.ArrayShape;
import core.data.InputOutputPair;
import core.data.exceptions.IllegalDataShapeException;
import core.data.exceptions.MismachedNumberOfInputsAndOutputsException;
import core.network_components.Transformer;
import core.network_components.error_functions.ErrorFunction;
import core.network_components.network_abstract.Network;
import utils.ListUtils;

import java.util.LinkedList;
import java.util.List;

public class LinearNetwork implements Network<ArrayData,ArrayData> {
    //public class Network {
    NodeLayer inputLayer;
    LinkedList<NodeLayer> layers = new LinkedList<>();
    Transformer output;
    ArrayShape inputShape;
    ArrayShape outputShape;
    ErrorFunction errorFunction;

    public ArrayData predict(ArrayData input) {
        if(!input.getShape().canReshapeTo(inputShape)) throw new IllegalDataShapeException(inputShape,input.getShape());
        input = input.reshape(inputShape);
//        NodeLayer inputDataLayer = input.getAsNodeLayer();

        for (int i = 0; i < input.getDimensionsUnsafe().size(); i++) {
//            layers.getFirst().nodes.get(i).set(inputDataLayer.nodes.get(i).emit());
            layers.getFirst().nodes.get(i).set(input.get(i).getData());
        }

//        layers.getFirst().nodes =
        layers.listIterator(1).forEachRemaining(NodeLayer::recieve);
        return layers.getLast().getAsArray();
    }

    @Override
    public void fitSingle(ArrayData input, ArrayData correctOutput, double lRate, ErrorFunction err) {
        ArrayData predictedOutput = predict(input);
        NodeLayer outputLayer = layers.getLast();
        for (int i = 0; i < outputLayer.nodes.size(); i++) {
            Node node = outputLayer.nodes.get(i);
            node.errorSignal = err.getError(correctOutput.getPoint(i),predictedOutput.getPoint(i),node.activation);
//            node.connectionsIn.forEach(c-> c.weight += node.errorSignal * c.start.emit() * lRate);
        }
//        predLayer.nodes.forEach(from -> layer.nodes.forEach(to -> new Connection(from, to)));

        ListUtils.reverserator(layers,1).forEachRemaining(l->l.nodes.forEach(n-> {
            n.errorSignal = 0;
            n.connectionsOut.forEach(c-> n.errorSignal+=c.end.errorSignal * c.weight);
            n.errorSignal *= n.activation.slopeAtY(n.emit());

        }));
        ListUtils.reverserator(layers).forEachRemaining(l->l.nodes.forEach(n->
                n.connectionsIn.forEach(c-> {
                    c.weight += n.errorSignal * c.start.emit() * lRate;
                })
        ));

    }

    @Override
    public void fitSetSingle(List<ArrayData> inputs, List<ArrayData> outputs, double lRate) {
        if(inputs.size()!=outputs.size()) throw new MismachedNumberOfInputsAndOutputsException(inputs.size(),outputs.size());
        for (int i = 0; i < inputs.size(); i++) {
            fitSingle(inputs.get(i),outputs.get(i),lRate);
        }
    }

    @Override
    public void fitSetSingle(List<InputOutputPair<ArrayData,ArrayData>> examples, double lRate) {
        examples.forEach(e->fitSingle(e.getInput(),e.getOutput(),lRate));
    }

    public void fitSetSingle(ArrayData inputs, ArrayData outputs, double lRate) {
        if(!inputs.getShape().chip().equals(inputShape)) throw new IllegalDataShapeException(inputShape,inputs.getShape().chip());
        if(!outputs.getShape().chip().equals(outputShape)) throw new IllegalDataShapeException(outputShape,inputs.getShape().chip());
        fitSetSingle(inputs.getDimensionsUnsafe(),outputs.getDimensionsUnsafe(),lRate);
    }

    public void addNodeLayer(int nodeCount) {
        NodeLayer layer = new NodeLayer(nodeCount);

        if(layers.size()==0) {
            inputShape = new ArrayShape(nodeCount);
        } else {
            NodeLayer previous = layers.getLast();
            //create connections
            previous.nodes.forEach(from -> layer.nodes.forEach(to -> new Connection(from, to)));
        }

        layers.add(layer);
        outputShape = layer.getShape();
    }

    public void learn(ArrayData inputs, ArrayData outputs) {
        if(!inputs.getShape().chip().equals(inputShape)) throw new IllegalDataShapeException(inputShape,inputs.getShape().chip());
        if(!outputs.getShape().chip().equals(outputShape)) throw new IllegalDataShapeException(outputShape,outputs.getShape().chip());
    }

    public ArrayShape getInputShape() {
        return inputShape;
    }
    public ArrayShape getOutputShape() {
        return outputShape;
    }
}



