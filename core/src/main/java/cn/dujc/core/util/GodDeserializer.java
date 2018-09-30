package cn.dujc.core.util;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 神级的gson反序列化处理类，会处理错误的json类型，取到类型错误也不报错，会继续取到剩余正确的值
 * 用法：Gson gson = new GsonBuilder().registerTypeAdapter(Bean2.class, new GodDeserializer<Bean>()).create()
 *
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

    /**
     * 将map中对应的数据拷贝到对象中，<b>对象需要初始化</b>
     *
     * @param object 对象，需要初始化
     * @param map    数据
     */
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

    /**
     * 设置变量的值，返回值代表是否需要继续尝试其他方案。正确设置值后，返回false，设置失败返回true
     *
     * @param field  变量
     * @param object 持有"变量"的对象
     * @param value  需要设置的值
     * @return 代表是否需要继续尝试其他方案，正确设置值后，返回false，设置失败返回true
     */
    private static void setFieldValue(Field field, Object object, Object value) {
        if (value == null) return;
        if (!field.isAccessible()) field.setAccessible(true);
        final Class<?> fieldType = field.getType();
        try {
            if (fieldType.isInstance(value)) {
                if (List.class.equals(fieldType)) {
                    final List values = (List) value;
                    final int size = values.size();
                    final List objects = new ArrayList(size);
                    for (int index = 0; index < size; index++) {
                        final Object val = values.get(index);
                        if (val == null) continue;
                        final Type genericType = field.getGenericType();
                        if (genericType instanceof ParameterizedType) {
                            final ParameterizedType pt = (ParameterizedType) genericType;
                            //得到泛型里的class类型对象
                            final Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
                            if (val instanceof Map) {
                                final Object o = genericClazz.newInstance();
                                copyFromMap(o, (Map<String, Object>) val);
                                objects.add(o);
                            } else {
                                final String valueStr = String.valueOf(val);
                                if (Integer.class.equals(genericClazz)) {
                                    final double number = Double.valueOf(valueStr);
                                    objects.add((int) number);
                                } else if (Float.class.equals(genericClazz)) {
                                    final double number = Double.valueOf(valueStr);
                                    objects.add((float) number);
                                } else if (Double.class.equals(genericClazz)) {
                                    final double number = Double.valueOf(valueStr);
                                    objects.add(number);
                                } else if (Short.class.equals(genericClazz)) {
                                    final double number = Double.valueOf(valueStr);
                                    objects.add((short) number);
                                } else if (Byte.class.equals(genericClazz)) {
                                    final double number = Double.valueOf(valueStr);
                                    objects.add((byte) number);
                                } else if (Boolean.class.equals(genericClazz)) {
                                    objects.add("true".equals(valueStr) || "1".equals(valueStr) || "1.0".equals(valueStr) || "yes".equalsIgnoreCase(valueStr));
                                } else if (Character.class.equals(genericClazz)) {
                                    objects.add(valueStr.trim().charAt(0));
                                } else if (boolean.class.equals(genericClazz)) {
                                    boolean b = "true".equals(valueStr) || "1".equals(valueStr) || "1.0".equals(valueStr) || "yes".equalsIgnoreCase(valueStr);
                                    objects.add(b);
                                } else if (char.class.equals(genericClazz)) {
                                    char c = valueStr.trim().charAt(0);
                                    objects.add(c);
                                } else if (int.class.equals(genericClazz)) {
                                    double number = Double.valueOf(valueStr);
                                    int i = (int) number;
                                    objects.add(i);
                                } else if (double.class.equals(genericClazz)) {
                                    double number = Double.valueOf(valueStr);
                                    objects.add(number);
                                } else if (float.class.equals(genericClazz)) {
                                    double number = Double.valueOf(valueStr);
                                    float f = (float) number;
                                    objects.add(f);
                                } else if (short.class.equals(genericClazz)) {
                                    double number = Double.valueOf(valueStr);
                                    short s = (short) number;
                                    objects.add(s);
                                } else if (byte.class.equals(genericClazz)) {
                                    double number = Double.valueOf(valueStr);
                                    byte b = (byte) number;
                                    objects.add(b);
                                } else if (genericClazz.isArray()) {
                                    final List inner = (List) value;
                                    final int innerSize = inner.size();
                                    final Class<?> componentType = genericClazz.getComponentType();
                                    final Object innerObjects = Array.newInstance(componentType, innerSize);
                                    for (int innerIndex = 0; innerIndex < innerSize; innerIndex++) {
                                        final Object innerVal = inner.get(innerIndex);
                                        if (innerVal == null) continue;
                                        if (innerVal instanceof Map) {
                                            final Object o = componentType.newInstance();
                                            copyFromMap(o, (Map<String, Object>) innerVal);
                                            Array.set(innerObjects, innerIndex, o);
                                        } else {
                                            putValue(innerObjects, innerIndex, innerVal, componentType);
                                        }
                                    }
                                    objects.add(innerObjects);
                                } else if (value instanceof Map) {
                                    final Object child = field.getType().newInstance();
                                    copyFromMap(child, (Map<String, Object>) value);
                                    objects.add(child);
                                }
                            }
                        }
                    }
                    field.set(object, objects);
                } else {
                    field.set(object, value);
                }
            } else if (fieldType.isPrimitive()) {
                final String valueStr = String.valueOf(value);
                if (boolean.class.equals(fieldType)) {
                    boolean b = "true".equals(valueStr) || "1".equals(valueStr) || "1.0".equals(valueStr) || "yes".equalsIgnoreCase(valueStr);
                    field.setBoolean(object, b);
                } else if (char.class.equals(fieldType)) {
                    char c = valueStr.trim().charAt(0);
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
                final List values = (List) value;
                final int size = values.size();
                final Class<?> componentType = fieldType.getComponentType();
                final Object objects = Array.newInstance(componentType, size);
                for (int index = 0; index < size; index++) {
                    final Object val = values.get(index);
                    if (val == null) continue;
                    if (val instanceof Map) {
                        final Object o = componentType.newInstance();
                        copyFromMap(o, (Map<String, Object>) val);
                        Array.set(objects, index, o);
                    } else {
                        putValue(objects, index, val, componentType);
                    }
                }
                field.set(object, objects);
            } else if (value instanceof Map) {
                final Object child = field.getType().newInstance();
                copyFromMap(child, (Map<String, Object>) value);
                field.set(object, child);
            } else {//Integer Float 等封装类不会到上面到基本类型中去
                final String valueStr = String.valueOf(value);
                if (Boolean.class.equals(fieldType)) {
                    field.set(object, "true".equals(valueStr) || "1".equals(valueStr) || "1.0".equals(valueStr) || "yes".equalsIgnoreCase(valueStr));
                } else if (Character.class.equals(fieldType)) {
                    field.set(object, valueStr.trim().charAt(0));
                } else {
                    final double number = Double.valueOf(valueStr);
                    if (Integer.class.equals(fieldType)) {
                        field.set(object, (int) number);
                    } else if (Float.class.equals(fieldType)) {
                        field.set(object, (float) number);
                    } else if (Double.class.equals(fieldType)) {
                        field.set(object, number);
                    } else if (Short.class.equals(fieldType)) {
                        field.set(object, (short) number);
                    } else if (Byte.class.equals(fieldType)) {
                        field.set(object, (byte) number);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断字符串是否表示正确
     */
    private static boolean valueIsTrue(String valueStr) {
        return "true".equals(valueStr) || "1".equals(valueStr) || "1.0".equals(valueStr) || "yes".equalsIgnoreCase(valueStr);
    }

    /**
     * 往数组中添加数据
     *
     * @param object        数组对象
     * @param index         位置
     * @param val           值
     * @param componentType 数组对象类型
     */
    private static void putValue(Object object, int index, Object val, Class<?> componentType) {
        if (val == null) return;
        if (componentType.isInstance(val)) {
            Array.set(object, index, val);
        } else if (componentType.isPrimitive()) {
            final String valueStr = String.valueOf(val);
            if (boolean.class.equals(componentType)) {
                boolean b = valueIsTrue(valueStr);
                Array.set(object, index, b);
            } else if (char.class.equals(componentType)) {
                char c = valueStr.trim().charAt(0);
                Array.set(object, index, c);
            } else {
                double number = Double.valueOf(valueStr);
                if (int.class.equals(componentType)) {
                    int i = (int) number;
                    Array.set(object, index, i);
                } else if (double.class.equals(componentType)) {
                    Array.set(object, index, number);
                } else if (float.class.equals(componentType)) {
                    float f = (float) number;
                    Array.set(object, index, f);
                } else if (short.class.equals(componentType)) {
                    short s = (short) number;
                    Array.set(object, index, s);
                } else if (byte.class.equals(componentType)) {
                    byte b = (byte) number;
                    Array.set(object, index, b);
                }
            }
        } else {//Integer Float 等封装类不会到上面到基本类型中去
            final String valueStr = String.valueOf(val);
            if (Boolean.class.equals(componentType)) {
                Array.set(object, index, valueIsTrue(valueStr));
            } else if (Character.class.equals(componentType)) {
                Array.set(object, index, valueStr.trim().charAt(0));
            } else {
                final double number = Double.valueOf(valueStr);
                if (Integer.class.equals(componentType)) {
                    Array.set(object, index, (int) number);
                } else if (Float.class.equals(componentType)) {
                    Array.set(object, index, (float) number);
                } else if (Double.class.equals(componentType)) {
                    Array.set(object, index, number);
                } else if (Short.class.equals(componentType)) {
                    Array.set(object, index, (short) number);
                } else if (Byte.class.equals(componentType)) {
                    Array.set(object, index, (byte) number);
                }
            }
        }
    }
}
