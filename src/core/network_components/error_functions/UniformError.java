package core.network_components.error_functions;

import core.network_components.activation_functions.ActivationFunction;

public class UniformError implements ErrorFunction {

    double errorVal;

    public UniformError(double error) {
        this.errorVal = error;
    }

    @Override
    public double getError(double correct, double predicted, ActivationFunction activation) {
        return errorVal;
    }
    public double getLoss(double correct, double predicted) {
        return errorVal;
    }
}
