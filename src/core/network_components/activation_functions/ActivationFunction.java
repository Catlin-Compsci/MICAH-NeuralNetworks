package core.network_components.activation_functions;

public interface ActivationFunction {
    public double getScaled(double unscaled);

    public default double slopeAtX(double x) {
//        double add = Double.MIN_NORMAL*100000d;
        double add = Math.pow(10d,-4d);
        return (getScaled(x+add)-getScaled(x)) / add;
    }
    public double slopeAtY(double y);
}
