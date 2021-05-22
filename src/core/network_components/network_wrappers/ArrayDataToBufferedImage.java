package core.network_components.network_wrappers;

import core.data.ArrayData;
import core.data.ArrayShape;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ArrayDataToBufferedImage implements Transformer<ArrayData, BufferedImage> {
    ArrayShape outputShape;
    PixelTransformer pixelTransformer;

    public ArrayDataToBufferedImage(PixelTransformer pixelTransformer, int... outputShape) {
        this.outputShape = new ArrayShape(outputShape);
        this.pixelTransformer = pixelTransformer;
    }

    @Override
    public BufferedImage transform(ArrayData input) {
        BufferedImage ret = new BufferedImage(outputShape.getDims()[0],outputShape.getDims()[1],BufferedImage.TYPE_BYTE_GRAY);
        input = input.reshape(outputShape);
        for (int y = 0; y < ret.getHeight(); y++) {
            for (int x = 0; x < ret.getWidth(); x++) {
                int intensity = pixelTransformer.reversed(input.getPoint(y,x));
                intensity = Math.max(Math.min(intensity,255),0);
//                System.out.println(intensity);
                ret.setRGB(x,y,new Color(intensity,intensity,intensity).getRGB());
            }
        }
        return ret;
    }
}
