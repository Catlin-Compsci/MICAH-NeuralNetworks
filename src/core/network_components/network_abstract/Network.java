package core.network_components.network_abstract;

import core.data.ArrayData;
import core.data.Data;
import core.data.InputOutputPair;
import core.network_components.error_functions.ErrorFunction;
import core.network_components.error_functions.ErrorSignal;

import java.util.List;

public interface Network<I extends Data,O extends Data> {
    public O predict(I input);
    public void fitSingle(I input, O output, double lRate, ErrorFunction err);
    public void fitSetSingle(List<I> inputs, List<O> outputs, double lRate);
    public void fitSetSingle(List<InputOutputPair<I,O>> examples, double lRate);
    public default void fitSingle(I input, O output, double lRate) {
        fitSingle(input,output,lRate,new ErrorSignal());
    }
}
