package core.network_components.network_wrappers;

import java.awt.*;

public class AlpaydinKaynakPixelTranfsormer implements PixelTransformer {

    @Override
    public Double transform(Double input) {
        return (double) (255 - input) * (double) 16 / (double) 256;
    }

    public int reversed(double arrayDataVal) {
        return (int)((arrayDataVal / ((double) 16 / (double) 256)) - 255) * -1;
    }
}
