package core.network_components.error_functions;

import core.network_components.activation_functions.ActivationFunction;

import java.util.Random;

public class NoisyUniformError extends UniformError {

    Random random = new Random();
    double randomRange;

    public NoisyUniformError(double averageError,double randomRange) {
        super(averageError);
        this.randomRange = randomRange;
    }

    @Override
    public double getError(double correct, double predicted, ActivationFunction activation) {
        return super.getError(correct, predicted, activation) + (random.nextBoolean() ? -1d : 1d) * random.nextDouble() * randomRange;
    }
}
