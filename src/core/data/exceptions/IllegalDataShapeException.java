package core.data.exceptions;

import core.data.ArrayShape;

import java.util.IllegalFormatException;

public class IllegalDataShapeException extends IllegalArgumentException {
    public IllegalDataShapeException(String message) {
        super(message);
    }
    public IllegalDataShapeException(ArrayShape expected, ArrayShape given) {
        this("Incorrect input data shape, expected: " + expected + " but given: " + given);
    }
}
