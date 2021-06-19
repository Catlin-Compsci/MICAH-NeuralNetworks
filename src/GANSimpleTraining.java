import core.data.ArrayData;
import core.data.ArrayDataSet;
import core.network_components.activation_functions.Linear;
import core.network_components.network_classes.LinearNetwork;
import core.network_components.network_wrappers.GANetwork;

import java.util.LinkedList;

public class GANSimpleTraining {
    public static void main(String... args) {

        LinkedList<ArrayData> data = new LinkedList<>();
//        for (int i = 0; i < 10; i++) {
//            data.add(ArrayData.of(1,2,3,4));
//        }
        data.add(ArrayData.of(1,2,3,4));
        data.add(ArrayData.of(2,4,6,8));
        data.add(ArrayData.of(4,8,12,16));
//        data.add(ArrayData.of(1,2,3,4));


        // Generator
        LinearNetwork g = new LinearNetwork(1);
        g.addNodeLayer(10);
        g.addNodeLayer(4,new Linear());
        // Discriminator
        LinearNetwork d = new LinearNetwork(4);
        d.addNodeLayer(10);
        d.addNodeLayer(2);

        GANetwork gan = new GANetwork(g,d);
        for (int i = 0; i < 100000; i++) {
            gan.fitEpoch(data,0.07);
            System.out.print(gan.generate() + "\r");
        }
//        System.out.println(gan.generate());
    }
}
