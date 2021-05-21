package core.data;

import java.util.HashMap;

public class InlineHashMap<K,V> extends HashMap<K,V> {
    public InlineHashMap<K,V> set(K key, V value) {
        put(key,value);
        return this;
    }
}
