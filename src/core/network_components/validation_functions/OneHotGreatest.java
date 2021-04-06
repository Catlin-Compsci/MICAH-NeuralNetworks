package core.network_components.validation_functions;

import core.data.ArrayData;

import java.util.List;

public class OneHotGreatest extends ValidationFunction {
    public OneHotGreatest(List<ArrayData> realY) {
        super(realY);
    }

    @Override
    public boolean validate(List<ArrayData> predictedY) {
        return false;
    }
}
