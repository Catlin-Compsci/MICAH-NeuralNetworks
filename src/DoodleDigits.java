import core.network_components.network_classes.LinearNetwork;
import graphics.DoodleDigitsWindow;

public class DoodleDigits {
    public static void main(String... args) {
        new DoodleDigitsWindow(LinearNetwork.load("data/networks/DIGITS-AK/2L-32,16.mpnn"));
//        new DoodleDigitsWindow(LinearNetwork.load("data/networks/DIGITS-AK/OneLayer32Nodes.mpnn"));
    }
}
