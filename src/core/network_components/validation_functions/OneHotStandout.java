package core.network_components.validation_functions;

import core.data.ArrayData;

public class OneHotStandout extends OneHotGreatest {

    double threshold;
    double difference;

    public OneHotStandout(double threshold, double difference) {
        super();
        this.threshold = threshold;
        this.difference = difference;
    }

    @Override
    public boolean validate(ArrayData predictedY, ArrayData real) {
        boolean valid = validatee(predictedY, real);
        System.out.println("DEBUG: predicted: " + predictedY + " real: " + real + " valid: " + valid);
        return valid;
    }

    private boolean validatee(ArrayData predictedY, ArrayData real) {
        assert predictedY.getShape().numDims() == 2 && real.getShape().numDims() == 2;
        double[] liv = super.largestIndexValue(predictedY);
        double i = liv[0];
        double v = liv[1];
        if (super.largestIndex(real) != i) return false;
        // is above threshold
        if (v < threshold) return false;
        // is diff num greater than peers
        for (int j = 0; j < predictedY.getDimensionsUnsafe().size(); j++) {
            if(j==i) continue;
            double val = predictedY.getPoint(j);
            if ((v - val) < difference) return false;
        }
        return true;
    }
}
