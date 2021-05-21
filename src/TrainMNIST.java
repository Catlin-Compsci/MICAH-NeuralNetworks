// Adapted from Andrew's code!

import core.data.*;
import core.network_components.network_classes.LinearNetwork;
import core.network_components.validation_functions.OneHotGreatest;
import core.network_components.validation_functions.OneHotStandout;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TrainMNIST {
    public static void main(String[] args) {

        // LOAD DATA
        System.out.print("Test loading...\r");
        long startTime = System.currentTimeMillis();
        ArrayDataSet testingExamples = readData("data/datasets/MNIST/t10k-labels.idx1-ubyte", "data/datasets/MNIST/t10k-images.idx3-ubyte");
        System.out.println("Test loaded in " + (System.currentTimeMillis() - startTime)/1000d + " seconds.");
        System.out.print("Train loading...\r");
        startTime = System.currentTimeMillis();
        ArrayDataSet trainingExamples = readData("data/datasets/MNIST/train-labels.idx1-ubyte", "data/datasets/MNIST/train-images.idx3-ubyte");
        System.out.println("Train loaded in " + (System.currentTimeMillis() - startTime)/1000d + " seconds.");
        System.out.println("Input image shape: " + testingExamples.inputShape());

        System.out.println(trainingExamples.get(100).getInput());
        System.out.println(trainingExamples.get(100).getOutput());

        System.exit(-1);
        // BUILD NETWORK
//        LinearNetwork nn = new LinearNetwork(trainingExamples.inputShape());
//        nn.addNodeLayer(500);
//        nn.addNodeLayer(trainingExamples.outputShape());
        System.out.print("Loading network...\r");
        startTime = System.currentTimeMillis();
        LinearNetwork nn = LinearNetwork.load("data/networks/MNIST/500Hidden.mpnn");
        System.out.println("Network loaded in " + (System.currentTimeMillis() - startTime)/1000d + " seconds.");

        // TRAIN
        nn.fitUntilValidated(trainingExamples,0.03,new OneHotGreatest(),0.95);
//        nn.fitUntilValidated(trainingExamples,0.03,new OneHotStandout(0.75,0.4),0.8);
        System.out.println("TEST ACCURACY: " + nn.testProportionValid(testingExamples,new OneHotGreatest()));
        nn.save("data/networks/MNIST/500HiddenAcc.mpnn");
        nn.fitUntilValidated(trainingExamples,0.03,new OneHotGreatest(),0.97);
//        nn.fitUntilValidated(trainingExamples,0.03,new OneHotStandout(0.75,0.4),0.8);
        System.out.println("TEST ACCURACY: " + nn.testProportionValid(testingExamples,new OneHotGreatest()));
        nn.save("data/networks/MNIST/500HiddenAcc.mpnn");
        nn.fitUntilValidated(trainingExamples,0.03,new OneHotGreatest(),0.99);
//        nn.fitUntilValidated(trainingExamples,0.03,new OneHotStandout(0.75,0.4),0.8);
        System.out.println("TEST ACCURACY: " + nn.testProportionValid(testingExamples,new OneHotGreatest()));
        nn.save("data/networks/MNIST/500HiddenAcc.mpnn");


    }

    static ArrayDataSet readData(String labelFileName, String imageFileName) {
        DataInputStream labelStream = openFile(labelFileName, 2049);
        DataInputStream imageStream = openFile(imageFileName, 2051);

        ArrayDataSet examples = new ArrayDataSet();

        try {
            int numLabels = labelStream.readInt();
            int numImages = imageStream.readInt();
            assert(numImages == numLabels) : "lengths of label file and image file do not match";

            int rows = imageStream.readInt();
            int cols = imageStream.readInt();
            assert(rows == cols) : "images in file are not square";
            assert(rows == 28) : "images in file are wrong size";

            for (int i = 0; i < numImages; i++) {
                int categoryLabel = Byte.toUnsignedInt(labelStream.readByte());
//                System.out.println((rows * cols));
                double[] inputs = new double[rows * cols];
//                double[] inputs = new double[rows * cols];
                for (int r = 0; r < rows; r++) {
                    for (int c = 0; c < cols; c++) {
                        int pixel = 255 - Byte.toUnsignedInt(imageStream.readByte());
                        inputs[r * rows + c] = (pixel / 255.0);
//                        inputs[r * rows + c] = pixel / 255.0;
                    }
                }
                double[] output = new double[10];
                output[categoryLabel] = 1;
                examples.add(new ArrayPair(ArrayArrayData.of(inputs),ArrayArrayData.of(output)));
//                examples.add(new ArrayPair(inputs,ArrayData.of(output)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return examples;
    }

    static DataInputStream openFile(String fileName, int expectedMagicNumber) {
        DataInputStream stream = null;
        try {
            stream = new DataInputStream(new BufferedInputStream(new FileInputStream(fileName)));
            int magic = stream.readInt();
            if (magic != expectedMagicNumber) {
                throw new RuntimeException("file " + fileName + " contains invalid magic number");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("file " + fileName + " was not found");
        } catch (IOException e) {
            throw new RuntimeException("file " + fileName + " had exception: " + e);
        }
        return stream;
    }
}
