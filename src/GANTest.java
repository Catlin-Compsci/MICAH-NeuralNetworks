import core.data.ArrayData;
import core.network_components.activation_functions.Linear;
import core.network_components.network_classes.LinearNetwork;
import core.network_components.network_wrappers.GANetwork;

public class GANTest {
    public static void main(String... args) {

        ArrayData lolRandomExample = ArrayData.of(4,3,2,4);

        // Generator
        LinearNetwork g = new LinearNetwork(1);
        g.addNodeLayer(10);
        g.addNodeLayer(4,new Linear());
        // Tester
        LinearNetwork d = new LinearNetwork(4);
        d.addNodeLayer(10);
        d.addNodeLayer(2);

        // Construct gan
        GANetwork gan = new GANetwork(g,d);

        // Train gan
        for (int i = 0; i < 100000; i++) {
            gan.fitSingle(lolRandomExample,0.03);
        }

        System.out.println(gan.generate());
        System.out.println(gan.discriminator.predict(lolRandomExample));
        System.out.println(gan.discriminator.predict(ArrayData.of(0,0,0,0)));
    }
}
