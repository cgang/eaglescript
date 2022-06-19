package org.eaglescript.util;

import org.eaglescript.ArrayObject;
import org.eaglescript.OrdinaryObject;
import org.eaglescript.vm.ScriptAwareException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JSONSupportTest {
    private JSONSupport support = new JSONSupport();

    @Test
    public void testStringify() throws ScriptAwareException {
        OrdinaryObject object = new OrdinaryObject();
        object.set("foo", "bar");

        ArrayObject array = new ArrayObject();
        array.add("e1");
        array.add("e2");

        OrdinaryObject child = new OrdinaryObject();
        child.set("c1", "foo");
        child.set("c2", 1234);

        array.add(child);
        object.set("array", array);

        assertEquals("{\"foo\":\"bar\",\"array\":[\"e1\",\"e2\",{\"c1\":\"foo\",\"c2\":1234}]}", support.stringify(object));
    }

    @Test
    public void testParse() throws ScriptAwareException {
        String json = "{\"foo\":\"bar\",\"array\":[\"e1\",\"e2\",{\"c1\":\"foo\",\"c2\":1234}]}";
        Object obj = support.parse(json);
        assertNotNull(obj);

        assertEquals(json, support.stringify(obj));
    }
}