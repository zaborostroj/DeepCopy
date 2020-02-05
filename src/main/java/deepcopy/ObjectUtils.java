package deepcopy;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import org.objenesis.instantiator.sun.SunReflectionFactoryInstantiator;

public class ObjectUtils {

    private Map<String, Object> objectsStorage = new HashMap<>();

    public Object copy(Object initialObject) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
        Object resultObject;
        String initialObjectId = Integer.toHexString(System.identityHashCode(initialObject)); // https://www.nomachetejuggling.com/2008/06/04/getting-a-java-objects-reference-id/
        Class<?> initialClass = initialObject.getClass();

        if (!objectsStorage.containsKey(initialObjectId)) {

            /*
               Инстанцирование объекта при отсутствии дефолтного конструктора.
               Подсмотрено в http://objenesis.org/
               SunReflectionFactoryInstantiator создаёт экземпляр объекта без вызова конструктора.
               Есть и другие типы инстантиаторов под разные JVM
            */
            resultObject = new SunReflectionFactoryInstantiator<>(initialClass).newInstance();

            objectsStorage.put(initialObjectId, resultObject);

            for (Field field : initialClass.getDeclaredFields()) {
                field.setAccessible(Boolean.TRUE);
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                if (Modifier.isFinal(field.getModifiers())) {
                    Field modifiers = Field.class.getDeclaredField("modifiers");
                    modifiers.setAccessible(true);
                    modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                }
                field.set(resultObject, copyField(field, initialObject));
            }
        } else {
            resultObject = objectsStorage.get(initialObjectId);
        }

        return resultObject;
    }

    private <T> T copyField(Field field, Object initialObject) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
        Class<?> fieldClass = field.getType();
        T fieldToCopy = (T) field.get(initialObject);
        // TODO: сделать ветку для полей-массивов
        if (fieldToCopy == null) {
            return null;
        } else if (fieldClass.isPrimitive() || isSimpleField(field)) {
            return fieldToCopy;
        } else {
            return (T) copy(field.get(initialObject));
        }
    }

    /**
     * Введено для упрощения задачи
     */
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
