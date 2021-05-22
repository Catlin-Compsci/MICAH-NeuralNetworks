import core.data.ArrayData;
import core.data.ArrayDataSet;
import core.data.InputOutputPair;
import core.network_components.activation_functions.Linear;
import core.network_components.network_classes.LinearNetwork;
import core.network_components.network_wrappers.AlpaydinKaynakPixelTranfsormer;
import core.network_components.network_wrappers.ArrayDataToBufferedImage;
import core.network_components.network_wrappers.GANetwork;
import graphics.GANWindow;
import utils.ArrayUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class GanAKDigits {
    public static void main(String... args) throws FileNotFoundException {

        // LOAD EXAMPLE IMAGES
        // TODO incorporate label data!??
        Scanner trainFile = new Scanner(new File("data/datasets/DIGITS-AK/digits-train.txt"));
        List<ArrayData> exampless = new LinkedList<>();
        trainFile.forEachRemaining(l -> {
            double[] data = Arrays.stream(l.split(",")).mapToDouble(s -> Double.parseDouble(s)).toArray();
            exampless.add(ArrayData.of(ArrayUtils.subsection(data,0,64)));
        });
//        List<ArrayData> examples = exampless.subList(37,38);
        List<ArrayData> examples = exampless.subList(100,200);

        // Generator
        LinearNetwork g = new LinearNetwork(5);
        g.addNodeLayer(32);
        g.addNodeLayer(86);
        g.addNodeLayer(64,new Linear());
        // Discriminator
        LinearNetwork d = new LinearNetwork(64);
//        d.addNodeLayer(80);
        d.addNodeLayer(40);
        d.addNodeLayer(2);

        GANetwork gan = new GANetwork(g,d);
//        for (int i = 0; i < 100000; i++) {
//            gan.fitEpoch(examples,0.07);
//            System.out.print(gan.generate() + "\r");
//        }
        new GANWindow(gan,examples,0.3,new ArrayDataToBufferedImage(new AlpaydinKaynakPixelTranfsormer(),8,8));
    }
}
