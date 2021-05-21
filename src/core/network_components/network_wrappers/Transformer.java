package core.network_components.network_wrappers;

//O = Output Type
public interface Transformer<I,O> {
    public O transform(I input);
}
