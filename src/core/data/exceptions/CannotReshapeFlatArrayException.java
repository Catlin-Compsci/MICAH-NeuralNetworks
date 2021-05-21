package core.data.exceptions;

import core.data.ArrayShape;

public class CannotReshapeFlatArrayException extends CannotReshapeArrayException{
    public CannotReshapeFlatArrayException(ArrayShape from, ArrayShape to) {
        super("Cannot reshape flat ArrayData " + from + " to " + to);
    }
}
