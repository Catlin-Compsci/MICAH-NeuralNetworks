package core.network_components.network_wrappers;

import core.data.ArrayData;
import core.data.ArrayShape;
import core.network_components.error_functions.ErrorSignal;
import core.network_components.network_abstract.GenerativeNetwork;
import core.network_components.network_classes.LinearNetwork;

import java.util.List;
import java.util.Random;

// Generative adversarial network
public class GANetwork implements GenerativeNetwork {
    public LinearNetwork generator;
    public LinearNetwork discriminator;
    private Random random = new Random();

    public static ArrayData REAL = ArrayData.of(1,0);
    public static ArrayData GENERATED = ArrayData.of(0,1);

    public GANetwork(LinearNetwork generator, LinearNetwork discriminator) {
        this.generator = generator;
        this.discriminator = discriminator;
    }

    public GANetwork(int productDims, int[] generatorHiddenLayers, int[] discriminatorHiddenLayers) {
        this(new ArrayShape(productDims),generatorHiddenLayers,discriminatorHiddenLayers);
    }

    public GANetwork(ArrayShape productShape, int[] generatorHiddenLayers, int[] discriminatorHiddenLayers) {
        // Create generator
        // INPUT -> one random seed number
        // OUTPUT -> product
        generator = new LinearNetwork(1);
        for (int layer : generatorHiddenLayers) {
            generator.addNodeLayer(layer);
        }
        generator.addNodeLayer(productShape.numPoints());

        // Create generator
        // INPUT -> product
        // OUTPUT -> onehot is real or not
        generator = new LinearNetwork(productShape.numPoints());
        for (int layer : discriminatorHiddenLayers) {
            generator.addNodeLayer(layer);
        }
        generator.addNodeLayer(2);
    }

    public ArrayData generate() {
        return generate(getNoise());
    }
    public ArrayData generate(double seed) {
        return generate(ArrayData.of(seed));
    }
    public ArrayData generate(ArrayData seed) {
        return generator.predict(seed);
    }

    public void fitEpoch(List<ArrayData> examples, double lRate) {
        discriminatorEpoch(examples,lRate);
        generatorEpoch(examples.size(),lRate);
    }

    public void discriminatorEpoch(List<ArrayData> examples, double lRate) {
        for (ArrayData example : examples) {
            discriminator.fitSingle(example,REAL,lRate);
            discriminator.fitSingle(generate(),GENERATED,lRate);;
        }
    }
    
    public void generatorEpoch(int num, double lRate) {
        for (int i = 0; i < num; i++) {
            ArrayData g = generate();
            ArrayData d = discriminator.predict(g);
            // Todo implement an actual GAN error function! Categorical crossentropy?
            discriminator.calculateError(d,REAL,new ErrorSignal());
            discriminator.propagateError();
            discriminator.propagateTo(generator);
            generator.propagateError();
            generator.updateWeights(lRate);
        }
    }

    // IDEA: Calculate the sensor errorsignals for the discriminator, and use those to set the generator initial errors
//    public void fitSingle(ArrayData example, double lRate) {
//        // Teach discriminator real
//        discriminator.fitSingle(example,REAL,lRate);
//
//        // Teach generator
//        ArrayData generated = generator.predict(getNoise());
//        ArrayData discrimination = discriminator.predict(generated);
//        // REAL will have error of 0, GENERATED will have error of 1 -- highest indexes are 0 or 1
//        generator.fitSingleAlreadyRun(generated,generated,lRate,new UniformError(OneHotGreatest.largestIndexValue(discrimination)[0]));
////        generator.fitSingleAlreadyRun(generated,example,lRate);
//
//        // Teach discriminator generated
//        discriminator.fitSingleAlreadyRun(discrimination,GENERATED,lRate);
//    }
    
    
    private ArrayData getNoise() {
        return ArrayData.of(random.nextDouble());
    }
}
