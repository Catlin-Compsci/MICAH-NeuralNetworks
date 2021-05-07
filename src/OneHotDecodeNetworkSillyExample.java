import core.data.ArrayData;
import core.data.ArrayDataSet;
import core.data.InputOutputPair;
import core.network_components.activation_functions.Linear;
import core.network_components.network_classes.LinearNetwork;
import core.network_components.validation_functions.SingleAbsoluteDifference;

public class OneHotDecodeNetworkSillyExample {
    public static void main(String... args) {
        ArrayDataSet encodedToNumber = new ArrayDataSet();
        for (int i = 0; i < 10; i++) {
            double[] arr = new double[10];
            arr[i] = 1;
            encodedToNumber.add(new InputOutputPair<>(ArrayData.of(arr),ArrayData.of(i)));
        }
        LinearNetwork decode = new LinearNetwork(10);
        decode.addNodeLayer(1, new Linear());
        decode.fitUntilValidated(encodedToNumber,0.5,new SingleAbsoluteDifference(0),1);
        ArrayData testeee = ArrayData.of(0,1,0,0,0,0,0,0,0,0);
        System.out.println(testeee + " => " + decode.predict(testeee));
        testeee = ArrayData.of(0,0,0,0,0,0,0,1,0,0);
        System.out.println(testeee + " => " + decode.predict(testeee));
        testeee = ArrayData.of(0,0,0,1,0,0,0,0,0,0);
        System.out.println(testeee + " => " + decode.predict(testeee));
//        decode.save("data/networks/DECODE-ONE-HOT (silly)/lol.mpnn");
    }
}
