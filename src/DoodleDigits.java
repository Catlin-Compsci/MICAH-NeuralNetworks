import core.network_components.network_classes.LinearNetwork;
import core.network_components.network_wrappers.AlpaydinKaynakPixelTranfsormer;
import core.network_components.network_wrappers.MNISTPixelTransformer;
import graphics.DoodleDigitsWindow;

public class DoodleDigits {
    public static void main(String... args) {
//        new DoodleDigitsWindow(LinearNetwork.load("data/networks/MNIST/1L-500=95A.mpnn"), new MNISTPixelTransformer());
        new DoodleDigitsWindow(LinearNetwork.load("data/networks/DIGITS-AK/1L-50-OneHotSt.mpnn"), new AlpaydinKaynakPixelTranfsormer());
//        new DoodleDigitsWindow(LinearNetwork.load("data/networks/DIGITS-AK/2L-32,16.mpnn"), new AlpaydinKaynakPixelTranfsormer());
//        new DoodleDigitsWindow(LinearNetwork.load("data/networks/DIGITS-AK/OneLayer32Nodes.mpnn"));
    }
}
