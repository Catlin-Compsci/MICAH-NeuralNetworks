package core.data;

import core.data.exceptions.MultiDimensionalArrayIndexOutOfBoundsException;
import core.data.exceptions.MultiDimensionalArrayIndexTooShortException;
import core.network_components.activation_functions.Linear;
import core.network_components.network_classes.BiasedNode;
import core.network_components.network_classes.Node;
import core.network_components.network_classes.NodeLayer;
import utils.ArrayUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ArrayData implements Data<ArrayShape> {
    private ArrayShape shape;
    ArrayList<ArrayData> dimensions;
    double data;
    private boolean isPoint;


    public static ArrayData linearFromArray(double[] points) {
        ArrayData ret = new ArrayData();
        for(double point : points) {
            ret.add(new ArrayData(point));
        }
        return ret;
    }

    public double getData() {
        return data;
    }

    public ArrayList<ArrayData> getDimensionsUnsafe() {
        return dimensions;
    }

    public ArrayData(ArrayShape shape) {
        this.shape = shape;
    }

    public ArrayData(double point) {
        isPoint = true;
        data = point;
        shape = new ArrayShape(1);
//        dimensions = new ArrayList<>();
    }

    public ArrayData(List<ArrayData> dataList) {
        dimensions = new ArrayList<>();
        dimensions.addAll(dataList);
        if(dataList.size()!=0) shape = dimensions.get(0).shape;
    }

    public ArrayData() {
        dimensions = new ArrayList<>();
        shape = null;
    }
    
    @Override
    public String toString() {
        StringBuilder ret= new StringBuilder();
        ret.append("[");
        AtomicInteger ind = new AtomicInteger(0);
        if(isPoint()) {
            ret.append(this.data);
        } else {
            dimensions.forEach((d)->{
                ind.incrementAndGet();
                if(d.isPoint()) {
                    if(ind.get() == dimensions.size()) ret.append(d.data + "");
                    else ret.append(d.data + ", ");
                } else {
                    ret.append(" ");
                    ret.append(d.toString());
                    if(ind.get() == dimensions.size()) ret.append(" ");
                    else ret.append(",");
                }
            });
        }

        ret.append("]");
        return ret.toString();
    }
    public void print() {
        System.out.println(this);
    }

    //TODO
    @Override
    public NodeLayer getAsNodeLayer() {
        ArrayData flattened = this.reshape(new ArrayShape(this.getShape().numPoints()));
        NodeLayer ret = new NodeLayer(0);
        for (ArrayData dimension : flattened.dimensions) {
            assert dimension.isPoint : "Internal data shape discontinuity";
            ret.addNode(new Node(dimension.data,new Linear()));
        }

        return ret;
    }

    public boolean isPoint() {
        return isPoint;
    }

    public ArrayShape getShape() {
        return shape;
    }

    public ArrayData reshape(ArrayShape newShape) {
        assert shape.canReshapeTo(newShape) : "cannot reshape to given shape, try extending or cropping instead";
        ArrayData newData = new ArrayData();
        if(newShape.equals(shape)) {
            return this;
        } else if(newShape.numDims()<shape.numDims()) {
            getListOfLayersWithShape(newShape.chip()).forEach(l->newData.add(l));
        } else {
//            MultiArray newData = new MultiArray(newShape);
//            List<ArrayData> units = getListOfLayersWithShape(new DataShape(1));
            //TODO FIX THIS AND MAKE IT CHIP INSTEAD OF WHATEVER IT DOES
            List<ArrayData> units = getListOfLayersWithShape(new ArrayShape(1));
//            List<ArrayData> units = getListOfLayersWithShape(new DataShape(newShape.getDims()[newShape.numDims()-1]));
            ArrayData container = null;
            for (int i = newShape.numDims()-2; i>=1 ; i--) {
                if(container!=null) units = container.dimensions;
                container = new ArrayData();
                int counter = 0;
                ArrayData toAdd = new ArrayData();
                for (ArrayData unit : units) {
                    counter++;
                    toAdd.add(unit);
                    if(counter%newShape.getDims()[i]==0) {
                        container.add(toAdd);
                        toAdd = new ArrayData();
                        counter = 0;
                    }
                }
            }
            container.dimensions.forEach(d-> newData.add(d));
        }
        return newData;
    }

    public List<ArrayData> getListOfLayersWithShape(ArrayShape s) {
        LinkedList<ArrayData> ret = new LinkedList<>();
        if(shape.equals(s)) {
            ret.add(this);
        } else {
            dimensions.forEach(dat->ret.addAll(dat.getListOfLayersWithShape(s)));
        }
        return ret;
    }

    public ArrayData get(ArrayIndex target) {
        for (int i = 0; i < target.numDims()-1; i++) {
            if(target.getDims()[i] >= shape.getDims()[i]) throw new MultiDimensionalArrayIndexOutOfBoundsException(shape,target);
        }
        ArrayData ret = this;
        for (int dim : target.getDims()) {
            ret = ret.dimensions.get(dim);
        }
        return ret;
    }
    public ArrayData get(int... target) {
        return get(new ArrayIndex(target));
    }

    public double getPoint(ArrayIndex target) {
        //Second part of and is stupid botch for stupid problem related to the fundemental problem of trimming/adding 1's
        //I really SHOULD be fixing the fundamental issue for this problem, but... meh lol
//        if(target.numDims()!=shape.numDims() - (shape.numDims()==1 ? 0 : 1) && !(shape.numPoints()==1 && target.numDims()==1)) throw new MultiDimensionalArrayIndexTooShortException(shape,target);
        if(target.numDims()!=shape.numDims() - (shape.numDims()==1 ? 0 : 1)) throw new MultiDimensionalArrayIndexTooShortException(shape,target);
        return get(target).data;
    }
    public double getPoint(int... target) {
        return getPoint(new ArrayIndex(target));
    }

    public void add(ArrayData data) {
        if(shape==null) shape = new ArrayShape(ArrayUtils.concatAll(new int[]{0},data.shape.getDims()));
        assert data.shape.equals(shape.chip());
        int[] newShapeArr = shape.getDims();
        newShapeArr[0]+=1;
        shape = new ArrayShape(newShapeArr);
        dimensions.add(data);
    }

    public void add(double point) {
        ArrayData p = new ArrayData(point);
        p.isPoint = true;

        add(p);
    }

}
