package core.data.exceptions;

public class MismachedNumberOfInputsAndOutputsException extends IllegalArgumentException{
    public MismachedNumberOfInputsAndOutputsException(int inputExamples,int outputExamples) {
        super("Expected same number of input examples and output examples, instead received " + inputExamples + " input examples and " + outputExamples + " output examples.");
    }
}
