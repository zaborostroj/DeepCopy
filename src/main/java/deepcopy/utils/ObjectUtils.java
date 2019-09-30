package deepcopy.utils;

import deepcopy.objects.Dummy;

import java.lang.reflect.Field;

public class ObjectUtils<T> {

    Object resultObject;

    public T copy(T initialObject) throws IllegalAccessException, InstantiationException {
        Class<?> initialClass = initialObject.getClass();
        resultObject = initialClass.newInstance();
        /* todo use java.lang.reflect.Constructor#newInstance(java.lang.Object...) Constructor.newInstance*/

        for (Field field : initialClass.getDeclaredFields()) {
            field.setAccessible(Boolean.TRUE);

            if (field.getType().equals(Byte.class) ||
                    field.getType().equals(Short.class) ||
                    field.getType().equals(Integer.class) ||
                    field.getType().equals(Long.class) ||
                    field.getType().equals(Float.class) ||
                    field.getType().equals(Double.class) ||
                    field.getType().equals(Boolean.class) ||
                    field.getType().equals(Character.class) ||
                    field.getType().equals(String.class)
            ) {
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
            if (subField.getType().equals(Byte.class) ||
                    subField.getType().equals(Short.class) ||
                    subField.getType().equals(Integer.class) ||
                    subField.getType().equals(Long.class) ||
                    subField.getType().equals(Float.class) ||
                    subField.getType().equals(Double.class) ||
                    subField.getType().equals(Boolean.class) ||
                    subField.getType().equals(Character.class) ||
                    subField.getType().equals(String.class)
            ) {
                subField.set(innerObjectCopy, subField.get(innerObject));
            } else /*if (subField.getType().equals(Dummy.class))*/ {
                subField.set(innerObjectCopy, copyFieldObject(subField, innerObject));
            }
        }

        return innerObjectCopy;
    }
}
