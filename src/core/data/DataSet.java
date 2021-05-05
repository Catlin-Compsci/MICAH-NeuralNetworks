package core.data;

import java.util.LinkedList;
import java.util.List;

public class DataSet<I extends Data,O extends Data> extends LinkedList<InputOutputPair<I, O>> {
    public List<I> getInputs() {
        LinkedList<I> inputs = new LinkedList<>();
        forEach(p->inputs.add(p.getInput()));
        return inputs;
    }
    public List<O> getOutputs() {
        LinkedList<O> outputs = new LinkedList<>();
        forEach(p->outputs.add(p.getOutput()));
        return outputs;
    }
}
