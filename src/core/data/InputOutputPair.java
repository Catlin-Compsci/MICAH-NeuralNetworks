package core.data;

public class InputOutputPair<I extends Data, O extends Data> {
    private I input;
    private O output;

    public InputOutputPair(I input, O output) {
        this.input = input;
        this.output = output;
    }

    public I getInput() {
        return input;
    }

    public O getOutput() {
        return output;
    }
}
