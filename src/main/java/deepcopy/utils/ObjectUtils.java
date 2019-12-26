package deepcopy.utils;

import deepcopy.objects.Dummy;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ObjectUtils<T> {

    private Object resultObject;
    private Map<String, Object> innerObjectsStorage = new HashMap<>();

    public Object copy2(Object initialObject) throws IllegalAccessException, InstantiationException {
        Class<?> initialClass = initialObject.getClass();
        resultObject = initialClass.newInstance();

        for (Field field : initialClass.getDeclaredFields()) {
            field.setAccessible(Boolean.TRUE);
            field.set(resultObject, copyField(field, initialObject));
        }

        // TODO:
        // если такого объекта нет, положить его в мапу
        // innerObjectsStorage.put("someObjectId", copied object);
        return resultObject;
    }

    private Object copyField(Field field, Object initialObject) throws IllegalAccessException, InstantiationException {
        Class<?> fieldClass = field.getType();
        Object fieldToCopy = field.get(initialObject);
        if (fieldToCopy == null) {
            return null;
        } else if (fieldClass.isPrimitive() || isSimpleField(field)) {
            return fieldToCopy;
        } else {
            // поискать поле-объект в мапе, если нет - слонировать и добавить в мапу
            copy2(field.get(initialObject));
        }

        return null;
    }

    private Boolean isSimpleField(Field field) {
        return
            field.getType().equals(Byte.class) ||
            field.getType().equals(Short.class) ||
            field.getType().equals(Integer.class) ||
            field.getType().equals(Long.class) ||
            field.getType().equals(Float.class) ||
            field.getType().equals(Double.class) ||
            field.getType().equals(Boolean.class) ||
            field.getType().equals(Character.class) ||
            field.getType().equals(String.class);
    }

    public T copy(T initialObject) throws IllegalAccessException, InstantiationException {
        Class<?> initialClass = initialObject.getClass();
        resultObject = initialClass.newInstance();
        /* todo use java.lang.reflect.Constructor#newInstance(java.lang.Object...) Constructor.newInstance*/

        for (Field field : initialClass.getDeclaredFields()) {
            field.setAccessible(Boolean.TRUE);

            if (isSimpleField(field)) {
                field.set(resultObject, field.get(initialObject));
            } else if (field.getType().isArray()) {
                System.out.println("array");
            } else if (field.getType().isEnum()) {
                System.out.println("enum");
            } else if (field.getType().equals(Dummy.class)) {
                field.set(resultObject, copyFieldObject(field, initialObject));
            }
        }

        return (T) resultObject;
    }

    private Object copyFieldObject(Field field, Object initialObject) throws IllegalAccessException, InstantiationException {
        Class<?> innerObjectClass = field.getType();
        Object innerObject = field.get(initialObject);
        Object innerObjectCopy = innerObjectClass.newInstance();

        for (Field subField : innerObjectClass.getDeclaredFields()) {
            subField.setAccessible(Boolean.TRUE);
            if (isSimpleField(field)) {
                subField.set(innerObjectCopy, subField.get(innerObject));
            } else /*if (subField.getType().equals(Dummy.class))*/ {
                subField.set(innerObjectCopy, copyFieldObject(subField, innerObject));
            }
        }

        return innerObjectCopy;
    }
}
