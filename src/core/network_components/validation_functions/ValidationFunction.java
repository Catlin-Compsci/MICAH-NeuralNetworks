package core.network_components.validation_functions;

import core.data.ArrayData;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class ValidationFunction {

    List<ArrayData> realY;

    public ValidationFunction(List<ArrayData> realY) {
        this.realY = realY;
    }

    public abstract boolean validate(ArrayData predictedY, ArrayData real);

    public interface ValidationFunctionFactory extends Function<List<ArrayData>,ValidationFunction> {
        public ValidationFunction apply(List<ArrayData> data);
    }

    public static final ValidationFunctionFactory oneHotGreatest = new ValidationFunctionFactory() {
        @Override
        public ValidationFunction apply(List<ArrayData> data) {
            return new OneHotGreatest(data);
        }
    };

    public double percentValidated(List<ArrayData> predicted, List<ArrayData> real) {
        assert real.size() == predicted.size();
        int correct = 0;
        for(int i = 0; i < predicted.size(); i++) {
            correct+=validate(predicted.get(i),real.get(i)) ? 1 : 0;
        }
        return correct/predicted.size();
    }

}
