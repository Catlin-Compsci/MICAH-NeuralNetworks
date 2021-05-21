package core.network_components.validation_functions;

import core.Config;
import core.data.ArrayData;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class ValidationFunction {

    public abstract boolean validate(ArrayData predictedY, ArrayData real);

//    public interface ValidationFunctionFactory extends Function<List<ArrayData>,ValidationFunction> {
//        public ValidationFunction apply(List<ArrayData> data);
//    }
//
//    public static final ValidationFunctionFactory oneHotGreatest = new ValidationFunctionFactory() {
//        @Override
//        public ValidationFunction apply(List<ArrayData> data) {
//            return new OneHotGreatest(data);
//        }
//    };

    public double percentValidated(List<ArrayData> predicted, List<ArrayData> real) {
        assert real.size() == predicted.size();
        double correct = 0;
        for(int i = 0; i < predicted.size(); i++) {
            if(Config.verbose) System.out.print("VALIDATING - Example (" + i + " / " + predicted.size() + ")\r");
            correct+=validate(predicted.get(i),real.get(i)) ? 1 : 0;
        }
        return correct/(double) predicted.size();
    }

}
