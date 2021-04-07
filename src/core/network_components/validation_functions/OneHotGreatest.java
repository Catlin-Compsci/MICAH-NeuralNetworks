package core.network_components.validation_functions;

import core.data.ArrayData;
import core.data.ArrayShape;

import java.util.List;

public class OneHotGreatest extends ValidationFunction {

    double threshold;
    double difference;

    public OneHotGreatest() {
        this(0,0);
    }
    public OneHotGreatest(double threshold, double difference) {
        super();
        this.threshold = threshold;
        this.difference = difference;
    }

    @Override
    public boolean validate(ArrayData predictedY, ArrayData real) {
        System.out.println("DEBUG: predicted numdims- " + predictedY.getShape().numDims());
        return largestIndex(predictedY) == largestIndex(real);
    }

    protected double[] largestIndexValue(ArrayData data) {
        int index = 0;
        double value = 0;
        for (int i = 0; i < data.getDimensionsUnsafe().size(); i++) {
            double daVal = data.getDimensionsUnsafe().get(i).getData();
            if(daVal < value) {
                value = daVal;
                index = i;
            }
        }
        return new double[]{index,value};
    }

    protected int largestIndex(ArrayData data) {
        return (int) largestIndexValue(data)[0];
    }


}
