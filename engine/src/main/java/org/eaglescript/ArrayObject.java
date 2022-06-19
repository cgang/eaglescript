package org.eaglescript;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link ArrayObject} represents an array exotic object.
 * @see <a href="https://262.ecma-international.org/6.0/#sec-array-exotic-objects">Array Exotic Objects</a>
 */
public class ArrayObject extends ScriptObject {
    private static final long serialVersionUID = 7284011699292500116L;

    private final List<Object> list = new ArrayList<>();

    public int size() {
        return list.size();
    }

    public void add(Object element) {
        list.add(element);
    }

    @Override
    public Object get(Object key) {
        if ("length".equals(key)) {
            return list.size();
        }
        return null;
    }

    public Object get(int index) {
        return list.get(index);
    }

    @Override
    public void set(Object key, Object value) {

    }
}
