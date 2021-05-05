import core.data.ArrayData;
import core.data.ArrayDataSet;
import core.data.InputOutputPair;
import core.network_components.network_classes.LinearNetwork;
import core.network_components.validation_functions.OneHotGreatest;
import core.network_components.validation_functions.OneHotStandout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Main {
    public static void main(String... args) {
        LinearNetwork nn = new LinearNetwork(2);
        nn.addNodeLayer(2);
        nn.addNodeLayer(2);

        ArrayDataSet trainData = new ArrayDataSet();
        trainData.add(new InputOutputPair(ArrayData.linearFromArray(new double[]{0,0}),ArrayData.linearFromArray(new double[]{0,1})));
        trainData.add(new InputOutputPair(ArrayData.linearFromArray(new double[]{1,0}),ArrayData.linearFromArray(new double[]{1,0})));
        trainData.add(new InputOutputPair(ArrayData.linearFromArray(new double[]{0,1}),ArrayData.linearFromArray(new double[]{1,0})));
        trainData.add(new InputOutputPair(ArrayData.linearFromArray(new double[]{1,1}),ArrayData.linearFromArray(new double[]{0,1})));
//        trainData.add(new InputOutputPair(ArrayData.linearFromArray(new double[]{1,1}),ArrayData.linearFromArray(new double[]{1})));


//        for (int i = 0; i < 25000; i++) {
//            nn.fitSetSingle(trainData,1);
//        }

        nn.fitUntilValidated(trainData,.05,new OneHotStandout(.9,0.5),1);
//        nn.fitUntilValidated(trainData,1,new OneHotGreatest(),1);

        ArrayData input = ArrayData.linearFromArray(new double[]{0,0});
        System.out.println(input);
        System.out.println(nn.predict(input));
        input = ArrayData.linearFromArray(new double[]{1,0});
        System.out.println(input);
        System.out.println(nn.predict(input));
        input = ArrayData.linearFromArray(new double[]{0,1});
        System.out.println(input);
        System.out.println(nn.predict(input));
        input = ArrayData.linearFromArray(new double[]{1,1});
        System.out.println(input);
        System.out.println(nn.predict(input));
        System.out.println("====================================");


        nn.save("data/networks/XOR/xor_2_hidden_nodes.mpnn");
        // .MPNN => Micah Powch Neural Network!
    }
}