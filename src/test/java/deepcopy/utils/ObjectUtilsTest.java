package deepcopy.utils;

import objects.SimpleFields;
import org.junit.Test;

public class ObjectUtilsTest {

    @Test
    public void simpleFieldsCopy() throws InstantiationException, IllegalAccessException {
        SimpleFields inputObject = new SimpleFields();

        ObjectUtils<SimpleFields> dummyCopy = new ObjectUtils<>();

        SimpleFields outputObject = dummyCopy.copy2(inputObject);

        inputObject.setPrimitiveIntValue(43);
        inputObject.setByteValue((byte) 43);
        inputObject.setShortValue((short) 43);
        inputObject.setIntegerValue(43);
        inputObject.setLongValue(43L);
        inputObject.setFloatValue(43.0F);
        inputObject.setDoubleValue(43.0);
        inputObject.setCharacterValue('3');
        inputObject.setStringValue("43");
        inputObject.setBooleanValue(Boolean.FALSE);

        assert outputObject.getByteValue().equals((byte) 42);
        assert outputObject.getPrimitiveIntValue() == 42;
        assert outputObject.getShortValue().equals((short) 42);
        assert outputObject.getIntegerValue().equals(42);
        assert outputObject.getLongValue().equals(42L);
        assert outputObject.getFloatValue().equals(42.0F);
        assert outputObject.getDoubleValue().equals(42.0);
        assert outputObject.getCharacterValue().equals('2');
        assert outputObject.getStringValue().equals("42");
        assert outputObject.getBooleanValue().equals(Boolean.TRUE);
    }
}