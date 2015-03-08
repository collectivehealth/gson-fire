package io.gsonfire.gson;

import com.google.gson.Gson;
import io.gsonfire.GsonFireBuilder;
import org.junit.Test;

/**
 * Created by julio on 3/8/15.
 */
public class EnumSerializationTest {

    @Test
    public void test() {
        Gson gson = new GsonFireBuilder()
            .createGson();

        String serialized = gson.toJson(AEnum.one);
        AEnum unserialized = gson.fromJson(serialized, AEnum.class);
    }


    public enum AEnum {

        one{
            @Override
            public int getNumeric() {
                return 1;
            }
        }, two{
            @Override
            public int getNumeric() {
                return 2;
            }
        };

        public String getNumber() {
            return this.name();
        }

        public abstract int getNumeric();

    }

}
