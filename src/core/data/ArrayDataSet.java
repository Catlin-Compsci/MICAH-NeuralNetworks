package core.data;

public class ArrayDataSet extends DataSet<ArrayData,ArrayData> {
    public ArrayShape inputShape() {
        return (size() == 0) ? null : get(0).getInput().getShape();
    }
    public ArrayShape outputShape() {
        return (size() == 0) ? null : get(0).getOutput().getShape();
    }
}
