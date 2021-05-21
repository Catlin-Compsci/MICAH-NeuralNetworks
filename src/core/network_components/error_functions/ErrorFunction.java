package core.network_components.error_functions;

import core.network_components.activation_functions.ActivationFunction;

public interface ErrorFunction {
    public double getError(double correct, double predicted, ActivationFunction activation);
    public double getLoss(double correct, double predicted);
}
