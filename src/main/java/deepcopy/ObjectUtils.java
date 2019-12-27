package deepcopy;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ObjectUtils {

    private Map<String, Object> objectsStorage = new HashMap<>();

    public Object copy2(Object initialObject) throws IllegalAccessException, InstantiationException {
        Object resultObject;
        String initialObjectId = Integer.toHexString(System.identityHashCode(initialObject)); // https://www.nomachetejuggling.com/2008/06/04/getting-a-java-objects-reference-id/
        Class<?> initialClass = initialObject.getClass();

        if (!objectsStorage.containsKey(initialObjectId)) {
            resultObject = initialClass.newInstance(); // http://objenesis.org/
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

    private <T> T copyField(Field field, Object initialObject) throws IllegalAccessException, InstantiationException {
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