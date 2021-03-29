package core.data;

public class InputOutputPair<I extends Data, O extends Data> {
    private O input;
    private I output;

    public InputOutputPair(O input, I output) {
        this.input = input;
        this.output = output;
    }

    public O getInput() {
        return input;
    }

    public I getOutput() {
        return output;
    }
}
