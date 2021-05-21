package core.data;

import core.data.exceptions.CannotReshapeArrayException;
import core.data.exceptions.CannotReshapeFlatArrayException;
import core.data.exceptions.MultiDimensionalArrayIndexTooShortException;
import core.network_components.activation_functions.Linear;
import core.network_components.network_classes.Node;
import core.network_components.network_classes.NodeLayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayArrayData extends ArrayData {
    double[] data;

    public static ArrayArrayData of(double... points) {
        return new ArrayArrayData(points);
    }

    public ArrayArrayData(int size) {
        data = new double[size];
        dimensions = new ArrayList<>();
    }

    public ArrayArrayData(double[] data) {
        this.data = data;
    }

    @Override
    public ArrayList<ArrayData> getDimensionsUnsafe() {
        if (dimensions == null) {
            System.out.println("Dimensions list remade [SUPER SLOW!!!!]");
            dimensions = new ArrayList<>(data.length);
            for (double d : data) {
                dimensions.add(new ArrayData(d));
            }
        }
        return dimensions;
    }

    @Override
    public String toString() {
        return Arrays.toString(data);
    }

    @Override
    public NodeLayer getAsNodeLayer() {
        NodeLayer ret = new NodeLayer(0);
        for (double n : data) {
            ret.addNode(new Node(n,new Linear()));
        }
        return ret;
    }

    @Override
    public ArrayData getFlattened() {
        return this;
    }

    @Override
    public double[] getAsDoubleArray() {
        return data;
    }

    @Override
    public ArrayShape getShape() {
        return new ArrayShape(data.length);
    }

    @Override
    public ArrayData reshape(ArrayShape newShape) {
        if(newShape.numDims() != 2) return null;
//        if(newShape.numDims() != 2) throw new CannotReshapeFlatArrayException(new ArrayShape(data.length),newShape);
        else if(newShape.numPoints() != data.length) return null;
//        else if(newShape.numPoints() != data.length) throw new CannotReshapeArrayException("Bro");
        else return this;
    }

    @Override
    public List<ArrayData> getListOfLayersWithShape(ArrayShape s) {
        return null;
    }

    @Override
    public ArrayData get(ArrayIndex target) {
        assert target.numDims() == 1 : "Cannot target multidimensional data in 1D array";
        return new ArrayData(data[target.getDims()[0]]);
    }
    @Override
    public double getPoint(ArrayIndex target) {
        //Second part of and is stupid botch for stupid problem related to the fundemental problem of trimming/adding 1's
        //I really SHOULD be fixing the fundamental issue for this problem, but... meh lol
//        if(target.numDims()!=shape.numDims() - (shape.numDims()==1 ? 0 : 1) && !(shape.numPoints()==1 && target.numDims()==1)) throw new MultiDimensionalArrayIndexTooShortException(shape,target);
        if(target.numDims()!=2 - (2==1 ? 0 : 1)) throw new MultiDimensionalArrayIndexTooShortException(shape,target);
        return data[target.getDims()[0]];
    }

    @Override
    public void add(double point) {
        System.out.println("ERROR!!!!! User tried to add to static ArrayArrayData!");
    }
}
