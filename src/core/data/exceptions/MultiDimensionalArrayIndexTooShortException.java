package core.data.exceptions;

import core.data.ArrayIndex;
import core.data.ArrayShape;

import java.util.Arrays;

public class MultiDimensionalArrayIndexTooShortException extends MultiDimensionalArrayIndexOutOfBoundsException {
    public MultiDimensionalArrayIndexTooShortException(ArrayShape underlying, ArrayIndex inputted) {
        super("Indeces: " + Arrays.toString(inputted.getDims()) + " too small for shape: " + Arrays.toString(underlying.getDims()));
    }
}