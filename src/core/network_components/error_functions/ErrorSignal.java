package core.network_components.error_functions;

import core.network_components.activation_functions.ActivationFunction;

public class ErrorSignal implements ErrorFunction {
    @Override
    public double getError(double correct, double predicted, ActivationFunction activation) {
        return (correct-predicted) * activation.slopeAtY(predicted);
    }
}
