package cn.dujc.core.util;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

/**
 * 神级的gson反序列化处理类，会处理错误的json类型，取到类型错误也不报错，会继续取到剩余正确的值
 * @author du
 * date 2018/7/14 上午11:21
 */
public class GodDeserializer<T> implements JsonDeserializer<T> {

    private static final Gson GSON = new Gson();
    // is number
    // is boolean
    // is object/map
    // is list
    // is array
    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        T result;
        try {
            result = GSON.fromJson(json, typeOfT);
        } catch (Exception e0) {
            e0.printStackTrace();
            try {
                result = (T) ((Class) typeOfT).newInstance();
                Map<String, Object> map = GSON.fromJson(json, new TypeToken<Map<String, Object>>() {}.getType());
                copyFromMap(result, map);
            } catch (Throwable e1) {
                e1.printStackTrace();
                result = null;
            }
        }
        return result;
    }

    private static void copyFromMap(Object object, Map<String, Object> map) {
        if (object == null || map == null || map.size() == 0) return;
        final Set<String> keys = map.keySet();
        final Class<?> clazz = object.getClass();
        for (String name : keys) {
            final Object value = map.get(name);
            try {
                final Field field = clazz.getDeclaredField(name);
                setFieldValue(field, object, value);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                final Field[] fields = clazz.getDeclaredFields();
                for (Field f : fields) {
                    final SerializedName serializedName = f.getAnnotation(SerializedName.class);
                    if (serializedName != null && name.equals(serializedName.value())) {
                        setFieldValue(f, object, value);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean setFieldValue(Field field, Object object, Object value) {
        boolean _continue = false;
        if (!field.isAccessible()) field.setAccessible(true);
        final Class<?> fieldType = field.getType();
        try {
            if (fieldType.isInstance(value)) {
                field.set(object, value);
            } else if (fieldType.isPrimitive()) {
                final String valueStr = String.valueOf(value);
                if (boolean.class.equals(fieldType)) {
                    boolean b = "true".equals(valueStr) || "1".equals(valueStr);
                    field.setBoolean(object, b);
                } else if (char.class.equals(fieldType)) {
                    char c = valueStr.charAt(0);
                    field.setChar(object, c);
                } else {
                    double number = Double.valueOf(valueStr);
                    if (int.class.equals(fieldType)) {
                        int i = (int) number;
                        field.setInt(object, i);
                    } else if (double.class.equals(fieldType)) {
                        field.setDouble(object, number);
                    } else if (float.class.equals(fieldType)) {
                        float f = (float) number;
                        field.setFloat(object, f);
                    } else if (short.class.equals(fieldType)) {
                        short s = (short) number;
                        field.setShort(object, s);
                    } else if (byte.class.equals(fieldType)) {
                        byte b = (byte) number;
                        field.setByte(object, b);
                    }
                }
            } else if (fieldType.isArray()) {
                //todo 暂时找不到数组怎么操作最完美，只好用gson转来转去来解决来
                field.set(object, GSON.fromJson(GSON.toJson(value), fieldType));
            } else if (value instanceof Map) {
                final Object child = field.getType().newInstance();
                copyFromMap(child, (Map<String, Object>) value);
                field.set(object, child);
            } else {//Integer Float 等封装类不会到上面到基本类型中去
                final double number = Double.valueOf(String.valueOf(value));
                if (Integer.class.equals(fieldType)) {
                    field.set(object, (int) number);
                } else if (Float.class.equals(fieldType)) {
                    field.set(object, (float) number);
                } else if (Double.class.equals(fieldType)) {
                    field.set(object, number);
                } else if (Short.class.equals(fieldType)) {
                    field.set(object, (short) number);
                } else if (Byte.class.equals(fieldType)) {
                    byte b = (byte) number;
                    field.set(object, b);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            _continue = true;
        }
        return _continue;
    }
}
