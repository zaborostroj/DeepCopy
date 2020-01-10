package deepcopy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisException;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

public class ObjectUtils {

    private Map<String, Object> objectsStorage = new HashMap<>();

    private Objenesis objenesis = new ObjenesisStd();

    public Object copy2(Object initialObject) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Object resultObject;
        String initialObjectId = Integer.toHexString(System.identityHashCode(initialObject)); // https://www.nomachetejuggling.com/2008/06/04/getting-a-java-objects-reference-id/
        Class<?> initialClass = initialObject.getClass();

        if (!objectsStorage.containsKey(initialObjectId)) {
//            resultObject = objenesis.newInstance(initialClass);
//            resultObject = initialClass.newInstance(); // http://objenesis.org/

            Class<?> reflectionFactoryClass = getReflectionFactoryClass();
            Object reflectionFactory = createReflectionFactory(reflectionFactoryClass);
            Method constructor = initialClass.getDeclaredMethod(
                "newConstructorForSerialization", Class.class, Constructor.class
            );
            resultObject = constructor.invoke(reflectionFactory, initialClass, constructor);
            objectsStorage.put(initialObjectId, resultObject);

            for (Field field : initialClass.getDeclaredFields()) {
                field.setAccessible(Boolean.TRUE);
                field.set(resultObject, copyField(field, initialObject));
            }
        } else {
            resultObject = objectsStorage.get(initialObjectId);
        }

        return resultObject;
    }

    private static Class<?> getReflectionFactoryClass() {
        try {
            return Class.forName("sun.reflect.ReflectionFactory");
        }
        catch(ClassNotFoundException e) {
            throw new ObjenesisException(e);
        }
    }

    private static Object createReflectionFactory(Class<?> reflectionFactoryClass) {
        try {
            Method method = reflectionFactoryClass.getDeclaredMethod(
                "getReflectionFactory");
            return method.invoke(null);
        }
        catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            throw new ObjenesisException(e);
        }
    }

    private <T> T copyField(Field field, Object initialObject) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<?> fieldClass = field.getType();
        T fieldToCopy = (T) field.get(initialObject);
        if (fieldToCopy == null) {
            return null;
        } else if (fieldClass.isPrimitive() || isSimpleField(field)) {
            return (T) fieldToCopy;
        } else {
            return (T) copy2(field.get(initialObject));
        }
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
}
