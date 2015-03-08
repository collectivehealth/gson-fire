package io.gsonfire.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by julio on 3/8/15.
 */
public final class IterableTypeAdapterFactory implements TypeAdapterFactory {

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Class<? super T> rawType = typeToken.getRawType();
        if (!Iterable.class.isAssignableFrom(rawType)) {
            return null;
        }
        if(Collection.class.isAssignableFrom(rawType)){
            return null;
        }

        final Type genericType;
        final Type type= typeToken.getType();
        if(type instanceof ParameterizedType) {
            genericType = ((ParameterizedType)type).getActualTypeArguments()[0];
        } else {
            genericType = Object.class;
        }

        TypeAdapter typeAdapter = gson.getAdapter(TypeToken.get(genericType));

        return newIterableAdapter(typeAdapter);
    }

    protected <T> TypeAdapter<T> newIterableAdapter(final TypeAdapter typeAdapter) {
        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T iterable) throws IOException {
                out.beginArray();
                for(Object o: (Iterable)iterable) {
                    typeAdapter.write(out, o);
                }
                out.endArray();
            }

            @Override
            public T read(JsonReader in) throws IOException {
                JsonToken next = in.peek();
                if(next == JsonToken.NULL) {
                    return null;
                } else {
                    List result = new ArrayList();
                    in.beginArray();
                    while(in.hasNext()) {
                        Object object = typeAdapter.read(in);
                        result.add(object);
                    }
                    in.endArray();
                    return (T) result;
                }
            }
        };
    }
}