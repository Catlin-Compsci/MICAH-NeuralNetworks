package core.network_components.validation_functions;

import core.data.ArrayData;
import core.data.ArrayShape;

import java.util.List;

public class OneHotGreatest extends ValidationFunction {


    @Override
    public boolean validate(ArrayData predictedY, ArrayData real) {
        assert predictedY.getShape().numDims() == 2 && real.getShape().numDims() == 2;
        boolean valid = largestIndex(predictedY) == largestIndex(real);
        System.out.println("DEBUG: predicted: " + predictedY + " real: " + real + " valid: " + valid);
        return valid;
    }

    protected double[] largestIndexValue(ArrayData data) {
        int index = 0;
        double value = 0;
        for (int i = 0; i < data.getDimensionsUnsafe().size(); i++) {
            double daVal = data.getDimensionsUnsafe().get(i).getData();
            if(value < daVal) {
                value = daVal;
                index = i;
            }
        } // christian schwab, christian schmitt
        return new double[]{index,value};
    }

    protected int largestIndex(ArrayData data) {
        return (int) largestIndexValue(data)[0];
    }
    protected double largestValue(ArrayData data) {
        return largestIndexValue(data)[1];
    }


}
