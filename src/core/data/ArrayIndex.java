package core.data;

import utils.ArrayUtils;

import java.util.Arrays;

public class ArrayIndex extends ArrayShape {
    public ArrayIndex(int... index) {
        this.dims = index;
    }
}
