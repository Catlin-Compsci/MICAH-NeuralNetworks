package core.network_components.network_wrappers;

public class MNISTPixelTransformer implements DoubleTransformer {
    @Override
    public Double transform(Double input) {
        return input/255d;
    }
}
