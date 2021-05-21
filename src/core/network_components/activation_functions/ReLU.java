package core.network_components.activation_functions;

public class ReLU implements ActivationFunction {
    @Override
    public double getScaled(double unscaled) {
        return Math.max(0,unscaled);
    }

    @Override
    public double slopeAtX(double x) {
        //TODO: rectify the left and right derivatives at x=0? naw lol
        return x < 0 ? 0d : 1d;
    }

    @Override
    public double slopeAtY(double y) {
        return y == 0 ? 0 : 1;
    }
}
