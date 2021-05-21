package core.network_components.activation_functions;

import core.data.InlineHashMap;

import java.util.HashMap;
import java.util.function.Supplier;

public interface ActivationFunction {

    public static final HashMap<String,ActivationFunction> loadMap = new InlineHashMap<String,ActivationFunction>()
            .set(null,new Logistic())
            .set("",new Logistic())
            .set("log",new Logistic())
            .set("lin",new Linear())
            .set("relu",new ReLU());
    public static final HashMap<Class<? extends ActivationFunction>,String> saveMap = new InlineHashMap<Class<? extends ActivationFunction>,String>()
            .set(Logistic.class,"log")
            .set(Linear.class,"lin")
            .set(ReLU.class,"relu");
    public interface ActivationFunctionFactory extends Supplier<ActivationFunction> {}

    public double getScaled(double unscaled);

    public default double slopeAtX(double x) {
//        double add = Double.MIN_NORMAL*100000d;
        double add = Math.pow(10d,-4d);
        return (getScaled(x+add)-getScaled(x)) / add;
    }
    public double slopeAtY(double y);
}
