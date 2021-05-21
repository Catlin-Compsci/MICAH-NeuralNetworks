package core.network_components.network_wrappers;

import java.awt.*;

public class AlpaydinKaynakPixelTranfsormer implements DoubleTransformer {
    @Override
    public Double transform(Double input) {
        return (double) (255 - input) * (double) 16 / (double) 256;
    }
}
