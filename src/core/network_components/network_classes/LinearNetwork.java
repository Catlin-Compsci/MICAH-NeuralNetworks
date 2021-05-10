package core.network_components.network_classes;

import core.data.ArrayData;
import core.data.ArrayDataSet;
import core.data.ArrayShape;
import core.data.InputOutputPair;
import core.data.exceptions.IllegalDataShapeException;
import core.data.exceptions.MismachedNumberOfInputsAndOutputsException;
import core.network_components.activation_functions.ActivationFunction;
import core.network_components.activation_functions.Logistic;
import core.network_components.error_functions.ErrorFunction;
import core.network_components.error_functions.ErrorSignal;
import core.network_components.network_abstract.Network;
import core.network_components.validation_functions.ValidationFunction;
import utils.ArrayUtils;
import utils.ListUtils;
import utils.NumUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// POSSIBLE WRONGIN'S
//
// Bias nodes not sending
// Bias nodes not propagating
// Bias nodes not existing
//
// HAHA!! YES!!! ERROR FOUND IN LIKE TWO SECONDS LMAO!!!!
// error was -- BiasNode inits with value zero, should have value one xDDD
// IM SO FRICKING HAPPY!!!!!
//
//
//
//
//
//
// Treating the input layer as a normal layer
//
// Error signals getting really small at the input of the network

public class LinearNetwork implements Network<ArrayData, ArrayData> {

    //    NodeLayer inputLayer;
    LinkedList<NodeLayer> layers = new LinkedList<>();
    SensorLayer sensorLayer;
    //    Transformer output;
    ArrayShape inputShape;
    ArrayShape outputShape;

    public LinearNetwork(int sensors) {
        this.sensorLayer = new SensorLayer(sensors);
        this.inputShape = new ArrayShape(sensors);
    }

    public LinearNetwork(ArrayShape inputShape) {
        this(inputShape.numPoints());
        this.inputShape = inputShape;
    }


    public ArrayData predict(ArrayData input) {
        if (!input.getShape().canReshapeTo(inputShape))
            throw new IllegalDataShapeException(inputShape, input.getShape());
        input = input.reshape(inputShape);
//        NodeLayer inputDataLayer = input.getAsNodeLayer();

        for (int i = 0; i < input.getDimensionsUnsafe().size(); i++) {
//            layers.getFirst().nodes.get(i).set(inputDataLayer.nodes.get(i).emit());
//            layers.getFirst().nodes.get(i).set(input.get(i).getData());
            sensorLayer.nodes.get(i).set(input.getPoint(i));
        }

//        layers.getFirst().nodes =
        layers.forEach(NodeLayer::recieve);
        return layers.getLast().getAsArray();
    }

    public List<ArrayData> predict(List<ArrayData> inputs) {
        LinkedList<ArrayData> predicted = new LinkedList<>();
        inputs.forEach(input -> predicted.add(predict(input)));
        return predicted;
    }


    // In this case, validation is:
    // ALL of the outputs [%true,%false]
    // being
    //
    //
    //
    //
    //
    //
    //
    //
    public int fitUntilValidated(List<ArrayData> inputs, List<ArrayData> outputs, double lRate, ValidationFunction validationFunction, double proportuneValid) {
        int epochs = 0;
        while (true) {
            epochs++;
            fitSetSingle(inputs, outputs, lRate);
            List<ArrayData> predicted = predict(inputs);
            double percentValid = validationFunction.percentValidated(predicted, outputs);
            if (percentValid >= proportuneValid) {
                System.out.println("Epoch: " + epochs + " completed | Validation: " + percentValid);
                break;
            }
            if (Integer.parseInt((String.valueOf(epochs) + "0").substring(1)) == 0) {
                System.out.println("Epoch: " + epochs + " completed | Validation: " + percentValid);
            }
        }
        System.out.println(proportuneValid + " validation accuracy met in " + epochs + " epochs");
        return epochs;
    }

    public int fitUntilValidated(ArrayDataSet trainingData, double lRate, ValidationFunction validationFunction, double validateProportion) {
        LinkedList<ArrayData> inputs = new LinkedList<>();
        LinkedList<ArrayData> outputs = new LinkedList<>();
        trainingData.forEach(pair -> {
            inputs.add(pair.getInput());
            outputs.add(pair.getOutput());
        });
        return fitUntilValidated(inputs, outputs, lRate, validationFunction, validateProportion);
    }

    public double testProportionValid(ArrayDataSet testData, ValidationFunction validationFunction) {
        return validationFunction.percentValidated(predict(testData.getInputs()), testData.getOutputs());
    }

    @Override
    public void fitSingle(ArrayData input, ArrayData correctOutput, double lRate, ErrorFunction err) {
        fitSingleAlreadyRun(predict(input),correctOutput,lRate,err);
    }

    public void fitSingleAlreadyRun(ArrayData predictedOutput, ArrayData correctOutput, double lRate) {
        fitSingleAlreadyRun(predictedOutput,correctOutput,lRate,new ErrorSignal());
    }

    public void fitSingleAlreadyRun(ArrayData predictedOutput, ArrayData correctOutput, double lRate, ErrorFunction err) {
        NodeLayer outputLayer = layers.getLast();
        for (int i = 0; i < outputLayer.nodes.size(); i++) {
            Node node = outputLayer.nodes.get(i);
            //TODO test that this is correct!
            node.errorSignal = err.getError(correctOutput.getPoint(i), predictedOutput.getPoint(i), node.activation);
//            node.connectionsIn.forEach(c-> c.weight += node.errorSignal * c.start.emit() * lRate);
        }
//        predLayer.nodes.forEach(from -> layer.nodes.forEach(to -> new Connection(from, to)));

        ListUtils.reverserator(layers, 1).forEachRemaining(l -> l.nodes.forEach(n -> {
            n.errorSignal = 0;
            n.connectionsOut.forEach(c -> n.errorSignal += c.end.errorSignal * c.weight);
            // TODO Switch this out
//            n.errorSignal *= n.activation.slopeAtX(n.getTotal());
            n.errorSignal *= n.activation.slopeAtY(n.emit());

        }));
        ListUtils.reverserator(layers).forEachRemaining(l -> l.nodes.forEach(n ->
                n.connectionsIn.forEach(c -> {
                    //todO check that ones in are ends
                    c.weight += n.errorSignal * c.start.emit() * lRate;
                })
        ));

    }

    @Override
    public void fitSetSingle(List<ArrayData> inputs, List<ArrayData> outputs, double lRate) {
        if (inputs.size() != outputs.size())
            throw new MismachedNumberOfInputsAndOutputsException(inputs.size(), outputs.size());
        for (int i = 0; i < inputs.size(); i++) {
            fitSingle(inputs.get(i), outputs.get(i), lRate);
        }
    }

    @Override
    public void fitSetSingle(List<InputOutputPair<ArrayData, ArrayData>> examples, double lRate) {
        examples.forEach(e -> fitSingle(e.getInput(), e.getOutput(), lRate));
    }

    public void fitSetSingle(ArrayData inputs, ArrayData outputs, double lRate) {
        if (!inputs.getShape().chip().equals(inputShape))
            throw new IllegalDataShapeException(inputShape, inputs.getShape().chip());
        if (!outputs.getShape().chip().equals(outputShape))
            throw new IllegalDataShapeException(outputShape, inputs.getShape().chip());
        fitSetSingle(inputs.getDimensionsUnsafe(), outputs.getDimensionsUnsafe(), lRate);
    }

    public void addNodeLayer(int nodeCount, ActivationFunction activation) {
        NodeLayer layer = new NodeLayer(nodeCount, activation);

        NodeLayer previous;
        if (layers.size() == 0) {
            previous = sensorLayer;
        } else {
            previous = layers.getLast();
            //create connections
        }
        previous.nodes.forEach(from -> layer.nodes.forEach(to -> new Connection(from, to)));

        layers.addLast(layer);
        outputShape = layer.getShape();
    }

    public void addNodeLayer(int nodeCount) {
        addNodeLayer(nodeCount,new Logistic());
    }

    public ArrayShape getInputShape() {
        return inputShape;
    }

    public ArrayShape getOutputShape() {
        return outputShape;
    }


    // Save file format:
    //
    // First line = each layer node count
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //

    public String saveString() {
        //HEADER (Network Shape)
        String ret = "";
        String comp = sensorLayer.nodes.size() + "";
        for (NodeLayer layer : layers) {
            comp += "," + layer.nodes.size();
        }
        ret += comp + "\n";

        //CONNECTION WEIGHTS
        //Connections into hidden/ans TODO Separate hidden from ans!
        for (NodeLayer layer : layers) {
            for (Node node : layer.nodes) {
                comp = "";
                for (Connection connection : node.connectionsIn) {
                    comp += connection.weight + ",";
                }
                ret += comp + "\n";
            }
        }
        return ret;
    }

    public void save(String fileName) {
        new File(fileName).getParentFile().mkdirs();
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.append(saveString());
            writer.close();
        } catch (IOException e) {
            System.out.println("Error opening file: " + e.getLocalizedMessage());
        }
    }

    public void loadWeights(String saveString) {
        String[] bois = saveString.split("\n");
        int i = 0;
        LinkedList<Double> vals = new LinkedList<>();
        for (String s : bois) {
            if (i == 0) {
                // TODO Network shape checking
            } else {
                for (String weight : s.trim().split(",")) {
                    vals.add(Double.parseDouble(weight));
                }
            }
            i++;
        }
        Iterator<Double> stream = vals.iterator();
        layers.forEach(l -> l.nodes.forEach(n -> n.connectionsIn.forEach(c -> c.weight = stream.next())));
    }

    public static LinearNetwork createFromString(String saveString) {
        int[] shape = Arrays.stream(saveString.split("\n")[0].split(",")).mapToInt(s -> Integer.parseInt(s)).toArray();
        LinearNetwork ret = new LinearNetwork(shape[0]);
        for (int i = 1; i < shape.length; i++) {
            ret.addNodeLayer(shape[i]);
        }
        ret.loadWeights(saveString);
        return ret;
    }

    public static LinearNetwork load(String filePath) {
        try {
//            return createFromString(new Scanner(LinearNetwork.class.getResource(filePath).openStream()).tokens().collect(Collectors.joining("\n")));
            return createFromString(new Scanner(new File(filePath)).tokens().collect(Collectors.joining("\n")));
        } catch (FileNotFoundException e) {
            System.out.println("Could not load NN file! > " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not load NN file! > " + e.getMessage());
        }
        return null;
    }
}



