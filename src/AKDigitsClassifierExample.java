import core.data.ArrayData;
import core.data.ArrayDataSet;
import core.data.InputOutputPair;
import core.network_components.network_classes.LinearNetwork;
import core.network_components.validation_functions.OneHotGreatest;
import core.network_components.validation_functions.OneHotStandout;
import utils.ArrayUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class AKDigitsClassifierExample {
    public static void main(String... args) throws FileNotFoundException {

        // LOAD TRAIN AND TEST DATA
        Scanner trainFile = new Scanner(new File("data/datasets/DIGITS-AK/digits-train.txt"));
        ArrayDataSet trainData = new ArrayDataSet();
        trainFile.forEachRemaining(l -> {
            double[] data = Arrays.stream(l.split(",")).mapToDouble(s -> Double.parseDouble(s)).toArray();
            double[] oneHotLabel = new double[10];
            oneHotLabel[(int) data[data.length - 1]] = 1;
            trainData.add(new InputOutputPair<>(ArrayData.from(ArrayUtils.subsection(data, 0, data.length - 1)), ArrayData.from(oneHotLabel)));
        });

        Scanner testFile = new Scanner(new File("data/datasets/DIGITS-AK/digits-test.txt"));
        ArrayDataSet testData = new ArrayDataSet();
        testFile.forEachRemaining(l -> {
            double[] data = Arrays.stream(l.split(",")).mapToDouble(s -> Double.parseDouble(s)).toArray();
            double[] oneHotLabel = new double[10];
            oneHotLabel[(int) data[data.length - 1]] = 1;
            testData.add(new InputOutputPair<>(ArrayData.from(ArrayUtils.subsection(data, 0, data.length - 1)), ArrayData.from(oneHotLabel)));
        });

        // BUILD NEURAL NETWORK!!!!
        LinearNetwork nn = new LinearNetwork(trainData.get(0).getInput().getShape());
        nn.addNodeLayer(32);
        nn.addNodeLayer(32);
        nn.addNodeLayer(trainData.get(0).getOutput().getShape().numPoints());


        nn.fitUntilValidated(trainData,0.06,new OneHotStandout(0.5,0.1),0.75);
        nn.save("data/networks/DIGITS-AK/initialTest.mpnn");
        System.out.println("TEST ACCURACY: " + nn.testProportionValid(testData, new OneHotGreatest()));
    }
}
