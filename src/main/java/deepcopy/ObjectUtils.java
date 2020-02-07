package deepcopy;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class ObjectUtils {

    private Map<String, Object> objectsStorage = new HashMap<>();

    public Object copy(Object initialObject) throws ReflectiveOperationException {
        Object resultObject;
        String initialObjectId = Integer.toHexString(System.identityHashCode(initialObject)); // https://www.nomachetejuggling.com/2008/06/04/getting-a-java-objects-reference-id/
        Class<?> initialClass = initialObject.getClass();

        if (!objectsStorage.containsKey(initialObjectId)) {

            resultObject = getNewInstance(initialClass);

            objectsStorage.put(initialObjectId, resultObject);

            for (Field field : initialClass.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                field.setAccessible(Boolean.TRUE);

//                if (Modifier.isFinal(field.getModifiers())) {
//                    Field modifiers = Field.class.getDeclaredField("modifiers");
//                    modifiers.setAccessible(true);
//                    modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
//                }

                field.set(resultObject, copyField(field, initialObject));
            }
        } else {
            resultObject = objectsStorage.get(initialObjectId);
        }

        return resultObject;
    }

    /**
     * Инстанцирование объекта при отсутствии дефолтного конструктора.
     * Подсмотрено в http://objenesis.org/
     * Метод создаёт экземпляр объекта без вызова конструктора.
     */
    private <T> T getNewInstance(Class<T> type) throws ReflectiveOperationException {
        Constructor<Object> javaLangObjectConstructor = Object.class.getConstructor((Class[]) null);
        Constructor<T> mungedConstructor = newConstructorForSerialization(type, javaLangObjectConstructor);
        mungedConstructor.setAccessible(true);
        return mungedConstructor.newInstance((Object[]) null);
    }

    @SuppressWarnings("unchecked")
    private <T> Constructor<T> newConstructorForSerialization(
        Class<T> type,
        Constructor<?> constructor
    ) throws ReflectiveOperationException {
        Class<?> reflectionFactoryClass = Class.forName("sun.reflect.ReflectionFactory");
        Object reflectionFactory = reflectionFactoryClass
            .getDeclaredMethod("getReflectionFactory")
            .invoke(null);
        Method newConstructorForSerializationMethod = reflectionFactoryClass.getDeclaredMethod(
            "newConstructorForSerialization", Class.class, Constructor.class);

        return (Constructor<T>) newConstructorForSerializationMethod.invoke(
            reflectionFactory, type, constructor);
    }

    @SuppressWarnings("unchecked")
    private <T> T copyField(Field field, Object initialObject)
        throws ReflectiveOperationException
    {
        Class<?> fieldClass = field.getType();
        T fieldToCopy = (T) field.get(initialObject);

        if (fieldToCopy == null) {
            return null;
        }

        if(fieldClass.isArray()) {
            return copyArrayField(fieldClass, fieldToCopy);
        }

        if (fieldClass.isPrimitive()) {
            return fieldToCopy;
        }

        return (T) copy(fieldToCopy);
    }

    @SuppressWarnings("unchecked")
    private <T> T copyArrayField(Class<?> fieldClass, T fieldToCopy) {
        int arraySize = Array.getLength(fieldToCopy);
        Object arrayFieldCopy = Array.newInstance(fieldClass.getComponentType(), arraySize);
        for (int i = 0; i < arraySize; i++) {
            Array.set(arrayFieldCopy, i, Array.get(fieldToCopy, i));
        }

        return (T) arrayFieldCopy;
    }
}
