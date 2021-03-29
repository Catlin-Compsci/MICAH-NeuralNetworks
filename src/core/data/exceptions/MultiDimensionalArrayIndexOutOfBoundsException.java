package core.data.exceptions;

import core.data.ArrayShape;

import java.util.Arrays;

public class MultiDimensionalArrayIndexOutOfBoundsException extends ArrayIndexOutOfBoundsException{
    public MultiDimensionalArrayIndexOutOfBoundsException(ArrayShape underlying, ArrayShape inputted) {
        super("Indeces: " + inputted + " out of bounds for shape: " + underlying);
    }
    protected MultiDimensionalArrayIndexOutOfBoundsException(String message) {
        super(message);
    }
}
