package core.network_components.activation_functions;

public class Logistic implements ActivationFunction{
    @Override
    public double getScaled(double unscaled) {
        return 1d/(1 + Math.exp(-unscaled));
    }

    @Override
    public double slopeAtX(double x) {
        return slopeAtY(getScaled(x));
    }

    @Override
    public double slopeAtY(double y) {
        return y*(1d-y);
    }
}
