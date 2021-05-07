package core.network_components.validation_functions;

import core.data.ArrayData;

public class SingleAbsoluteDifference extends ValidationFunction {

    double maxDif;

    public SingleAbsoluteDifference(double maxDifference) {
        this.maxDif = maxDifference;
    }

    @Override
    public boolean validate(ArrayData predictedY, ArrayData real) {
        return Math.abs(predictedY.getPoint(0) - real.getPoint(0)) <= maxDif;
    }
}
