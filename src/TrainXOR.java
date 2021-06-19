import core.Config;
import core.data.ArrayData;
import core.data.ArrayDataSet;
import core.data.InputOutputPair;
import core.network_components.activation_functions.Linear;
import core.network_components.activation_functions.ReLU;
import core.network_components.network_classes.LinearNetwork;
import core.network_components.validation_functions.OneHotGreatest;
import core.network_components.validation_functions.OneHotStandout;

public class TrainXOR {
    public static void main(String... args) {
        LinearNetwork nn = new LinearNetwork(2);
        nn.addNodeLayer(2);
        nn.addNodeLayer(2);

        ArrayDataSet trainData = new ArrayDataSet();
        trainData.add(new InputOutputPair(ArrayData.of(new double[]{0,0}),ArrayData.of(new double[]{0,1})));
        trainData.add(new InputOutputPair(ArrayData.of(new double[]{1,0}),ArrayData.of(new double[]{1,0})));
        trainData.add(new InputOutputPair(ArrayData.of(new double[]{0,1}),ArrayData.of(new double[]{1,0})));
        trainData.add(new InputOutputPair(ArrayData.of(new double[]{1,1}),ArrayData.of(new double[]{0,1})));

//        nn.fitUntilValidated(trainData,.05,new OneHotStandout(.9,0.5),1);
        nn.fitUntilValidated(trainData,0.08,new OneHotGreatest(),1);

        System.out.println("====================================");
        ArrayData input = ArrayData.of(new double[]{0,0});
        System.out.println("INPUT: " + input);
        System.out.println("OUTPUT: " + nn.predict(input));
        System.out.println("--");
        input = ArrayData.of(new double[]{1,0});
        System.out.println("INPUT: " + input);
        System.out.println("OUTPUT: " + nn.predict(input));
        System.out.println("--");
        input = ArrayData.of(new double[]{0,1});
        System.out.println("INPUT: " + input);
        System.out.println("OUTPUT: " + nn.predict(input));
        System.out.println("--");
        input = ArrayData.of(new double[]{1,1});
        System.out.println("INPUT: " + input);
        System.out.println("OUTPUT: " + nn.predict(input));
        System.out.println("====================================");


        // SAVE
//        nn.save("data/networks/XOR/xor_2_hidden_nodes.mpnn");
        // .MPNN => Micah Powch Neural Network!
    }
}