package io.gsonfire.gson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.gsonfire.GsonFireBuilder;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;

import static org.junit.Assert.assertEquals;

/**
 * Created by julio on 3/8/15.
 */
public class IterableTest {

    @Test
    public void testSerializeIntegers() {
        Gson gson = new GsonFireBuilder()
            .enableIterableSupport()
            .createGsonBuilder()
            .create();

        Iterable iterable = new GenericIterable(1, 2, 3);
        String json = gson.toJson(iterable);
        assertEquals("[1,2,3]", json);
    }

    @Test
    public void testDeserializeIntegers() {
        Gson gson = new GsonFireBuilder()
            .enableIterableSupport()
            .createGsonBuilder()
            .create();

        String json = "[1,2,3]";
        Iterable<Integer> iterable = gson.fromJson(json, new TypeToken<Iterable<Integer>>(){}.getType());

        LinkedHashSet<Integer> result = new LinkedHashSet<Integer>();
        for(Integer i: iterable) {
            result.add(i);
        }

        LinkedHashSet<Integer> expected = new LinkedHashSet<Integer>(Arrays.asList(1, 2, 3));
        assertEquals(expected, result);
    }

    @Test
    public void testSerializeObjects() {
        Gson gson = new GsonFireBuilder()
            .enableIterableSupport()
            .createGsonBuilder()
            .create();

        Iterable iterable = new GenericIterable(new StringContainer("a"), new StringContainer("b"));
        String json = gson.toJson(iterable);
        assertEquals("[{\"value\":\"a\"},{\"value\":\"b\"}]", json);
    }

    @Test
    public void testDeserializeObjects() {
        Gson gson = new GsonFireBuilder()
            .enableIterableSupport()
            .createGsonBuilder()
            .create();

        String json = "[{\"value\":\"a\"},{\"value\":\"b\"}]";
        Iterable<StringContainer> iterable = gson.fromJson(json, new TypeToken<Iterable<StringContainer>>(){}.getType());

        LinkedHashSet<StringContainer> result = new LinkedHashSet<StringContainer>();
        for(StringContainer s: iterable) {
            result.add(s);
        }

        LinkedHashSet<StringContainer> expected = new LinkedHashSet<StringContainer>(Arrays.asList(new StringContainer("a"), new StringContainer("b")));
        assertEquals(expected, result);
    }

    public class GenericIterable<T> implements Iterable<T> {

        private final T[] values;

        public GenericIterable(T... values) {
            this.values = values;
        }

        @Override
        public Iterator<T> iterator() {
            return Arrays.asList(this.values).iterator();
        }
    }

    public class StringContainer {

        public String value;

        public StringContainer(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            StringContainer that = (StringContainer) o;

            if (value != null ? !value.equals(that.value) : that.value != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }

}
