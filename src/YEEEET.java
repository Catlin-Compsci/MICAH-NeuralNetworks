import core.network_components.network_classes.LinearNetwork;
import core.network_components.validation_functions.OneHotGreatest;
import core.network_components.validation_functions.OneHotStandout;

public class YEEEET {
    public static void main(String... args) {
        LinearNetwork nn = new LinearNetwork(10);
        nn.addNodeLayer(124);
        nn.addNodeLayer(64);
        nn.addNodeLayer(2);

        // [0,0,0.2,0,.2,.7]
        // [0,0,0.0,0,0,1]

        nn.fitUntilValidated(null,0.03,new OneHotStandout(.75,.3),.95);
    }
}
