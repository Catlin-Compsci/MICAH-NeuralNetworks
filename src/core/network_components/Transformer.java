package core.network_components;

//O = Output Type
public interface Transformer<I,O> {
    public O transform(I input);
}
