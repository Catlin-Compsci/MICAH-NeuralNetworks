import core.Config;
import core.data.ArrayArrayData;
import core.data.ArrayData;
import core.data.ArrayDataSet;
import core.data.InputOutputPair;
import core.network_components.activation_functions.Linear;
import core.network_components.network_classes.LinearNetwork;
import core.network_components.validation_functions.OneHotGreatest;
import core.network_components.validation_functions.OneHotStandout;
import utils.ArrayUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class TrainAKDigitsClassifier {
    public static void main(String... args) throws FileNotFoundException {

        // LOAD TRAIN AND TEST DATA
        Scanner trainFile = new Scanner(new File("data/datasets/DIGITS-AK/digits-train.txt"));
        ArrayDataSet trainData = new ArrayDataSet();
        trainFile.forEachRemaining(l -> {
            double[] data = Arrays.stream(l.split(",")).mapToDouble(s -> Double.parseDouble(s)).toArray();
            double[] oneHotLabel = new double[10];
            oneHotLabel[(int) data[data.length - 1]] = 1;
            trainData.add(new InputOutputPair<>(ArrayData.of(ArrayUtils.subsection(data, 0, data.length - 1)), ArrayData.of(oneHotLabel)));
        });

        Scanner testFile = new Scanner(new File("data/datasets/DIGITS-AK/digits-test.txt"));
        ArrayDataSet testData = new ArrayDataSet();
        testFile.forEachRemaining(l -> {
            double[] data = Arrays.stream(l.split(",")).mapToDouble(s -> Double.parseDouble(s)).toArray();
            double[] oneHotLabel = new double[10];
            oneHotLabel[(int) data[data.length - 1]] = 1;
            testData.add(new InputOutputPair<>(ArrayData.of(ArrayUtils.subsection(data, 0, data.length - 1)), ArrayData.of(oneHotLabel)));
        });

        // BUILD NEURAL NETWORK!!!!
        Config.verbose = true;
        LinearNetwork nn = new LinearNetwork(trainData.get(0).getInput().getShape());
        nn.addNodeLayer(50);
//        nn.addNodeLayer(25);
//        nn.addNodeLayer(10);
//        nn.addNodeLayer(5);
//        nn.addNodeLayer(3);
        nn.addNodeLayer(trainData.get(0).getOutput().getShape().numPoints());

//        nn.fitUntilValidated(trainData,0.03,new OneHotStandout(0.5,0.1),0.98);
        nn.fitUntilValidated(trainData,0.03,new OneHotGreatest(),0.99);
//        nn.fitUntilValidated(trainData,1,new OneHotStandout(0.75,0.3),0.99);
//        nn.save("data/networks/DIGITS-AK/2L-50,50-OneHotSt.mpnn");
        System.out.println("TEST ACCURACY: " + nn.testProportionValid(testData,new OneHotGreatest()));
//        System.out.println("TEST ACCURACY: " + nn.testProportionValid(testData, new OneHotGreatest()));


//        System.out.println("TEST ACCURACY: " + digitsNetwork.testProportionValid(testData, new OneHotGreatest()));
//        System.out.println("TEST ACCURACY (strict verification): " + digitsNetwork.testProportionValid(testData, new OneHotStandout(0.5,0.1)));
//        System.out.println(">");
//        InputOutputPair<ArrayData,ArrayData> example = testData.get(334);
//        System.out.println("INPUT: " + example.getInput());
//        System.out.println("REAL ANSWER: " + example.getOutput());
//        System.out.println("PREDICTION: " + digitsNetwork.predict(example.getInput()));
    }
}
