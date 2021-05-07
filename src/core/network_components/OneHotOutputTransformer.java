package core.network_components;

import core.data.ArrayData;
import core.network_components.validation_functions.OneHotGreatest;

public class OneHotOutputTransformer<O> implements Transformer<ArrayData,O> {

    O[] indexMap;

    public OneHotOutputTransformer(O... indexMap) {
        this.indexMap = indexMap;
    }

    @Override
    public O transform(ArrayData input) {
        return indexMap[(int)OneHotGreatest.largestIndexValue(input)[0]];
    }
}
