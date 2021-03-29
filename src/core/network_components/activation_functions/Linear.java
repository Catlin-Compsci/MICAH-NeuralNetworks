package core.network_components.activation_functions;

public class Linear implements ActivationFunction{
    @Override
    public double getScaled(double unscaled) {
        return unscaled;
    }

    @Override
    public double slopeAtX(double x) {
        return 1;
    }

    @Override
    public double slopeAtY(double y) {
        return 1;
    }
}
