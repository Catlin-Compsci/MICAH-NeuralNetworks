import core.network_components.activation_functions.Linear;
import core.network_components.activation_functions.Logistic;
import core.network_components.activation_functions.ReLU;
import core.network_components.network_classes.LinearNetwork;
import core.network_components.validation_functions.OneHotStandout;

public class DifferentActivationFunctionsExample {
    public static void main(String... args) {
        LinearNetwork nn = new LinearNetwork(10);
        nn.addNodeLayer(40, new ReLU());
        nn.addNodeLayer(40, new Logistic());
        nn.addNodeLayer(20, new Linear());

        nn.fitUntilValidated(null,0.03,new OneHotStandout(.75,.3),.95);
    }
}
