package deepcopy.utils;

import deepcopy.objects.Dummy;

import java.lang.reflect.Field;

public class ObjectUtils<T> {

    Object resultObject;

    public T copy(T initialObject) throws IllegalAccessException, InstantiationException {
        Class<?> initialClass = initialObject.getClass();
        resultObject = initialClass.newInstance();

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
                Class<?> innerObjectClass = field.getType();
                Object innerObject = field.get(initialObject);
                Object innerObjectCopy = innerObjectClass.newInstance();

                for (Field subField : innerObjectClass.getDeclaredFields()) {
                    subField.setAccessible(Boolean.TRUE);
                    System.out.println(subField.get(innerObject));
                    subField.set(innerObjectCopy, subField.get(innerObject));
                }

//                for (Field subField : innerObjectClass.getDeclaredFields()) {
//                    subField.setAccessible(Boolean.TRUE);
//                    System.out.println(subField.getName() + " " + subField.get(innerObjectCopy));
//                    subField.set(innerObjectCopy, subField.get(innerObjectCopy));
//                }

                field.set(resultObject, innerObjectCopy);
            }
        }

        return (T) resultObject;
    }
}
