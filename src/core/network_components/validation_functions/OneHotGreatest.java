package core.network_components.validation_functions;

import core.data.ArrayData;
import core.data.ArrayShape;

import java.util.List;

public class OneHotGreatest extends ValidationFunction {
    public OneHotGreatest(List<ArrayData> realY) {
        super(realY);
    }

    @Override
    public boolean validate(ArrayData predictedY, ArrayData real) {
        System.out.println("DEBUG: predicted numdims- " + predictedY.getShape().numDims());
        return largestIndex(predictedY) == largestIndex(real);
    }

    private int largestIndex(ArrayData data) {
        int index = 0;
        double value = 0;
        for (int i = 0; i < data.getDimensionsUnsafe().size(); i++) {
            double daVal = data.getDimensionsUnsafe().get(i).getData();
            if(daVal < value) {
                value = daVal;
                index = i;
            }
        }
        return index;
    }
}
