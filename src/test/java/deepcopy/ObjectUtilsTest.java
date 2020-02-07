package deepcopy;

import objects.ClassA;
import objects.ClassB;
import objects.ClassC;
import objects.ClassD;
import objects.ObjectWithArray;
import objects.ObjectWithFinal;
import objects.ObjectWithSimpleFields;
import objects.ObjectWithStatic;
import objects.ObjectWithoutDefaultConstructor;
import org.junit.Test;

public class ObjectUtilsTest {

    @Test
    public void simpleFieldsCopy() throws ReflectiveOperationException {
        ObjectWithSimpleFields inputObject = new ObjectWithSimpleFields();

        ObjectUtils dummyCopy = new ObjectUtils();

        ObjectWithSimpleFields outputObject = (ObjectWithSimpleFields) dummyCopy.copy(inputObject);

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

    @Test
    public void innerClassesCopyTest() throws ReflectiveOperationException {
        ObjectUtils objectUtils = new ObjectUtils();

        ClassA inputA = new ClassA();
        ClassB instanceOfB = new ClassB();
        ClassC instanceOfC = new ClassC();
        ClassD instanceOfD1 = new ClassD();
        ClassD instanceOfD2 = new ClassD();

        instanceOfB.setClassD(instanceOfD1);
        instanceOfB.setClassA(inputA);
        instanceOfC.setClassD(instanceOfD1);
        inputA.setClassB(instanceOfB);
        inputA.setClassC(instanceOfC);
        inputA.setClassD(instanceOfD2);

        ClassA outputA = (ClassA) objectUtils.copy(inputA);

        //
        assert !getHexCode(inputA.getClassB())
            .equals(getHexCode(outputA.getClassB()));
        assert !getHexCode(inputA.getClassC())
            .equals(getHexCode(outputA.getClassC()));
        assert !getHexCode(inputA.getClassD())
            .equals(getHexCode(outputA.getClassD()));
        assert !getHexCode(inputA.getClassB().getClassD())
            .equals(getHexCode(outputA.getClassB().getClassD()));

        // ссылки на разные вложенные объекты одного типа
        assert !getHexCode(outputA.getClassB().getClassD())
            .equals(getHexCode(outputA.getClassD()));
        assert getHexCode(outputA.getClassB().getClassD())
            .equals(getHexCode(outputA.getClassC().getClassD()));

        // обратная ссылка из вложенного объекта на внешний
        assert getHexCode(outputA)
            .equals(getHexCode(outputA.getClassB().getClassA()));
    }

    private String getHexCode(Object o) {
        return Integer.toHexString(System.identityHashCode(o));
    }

    @Test
    public void objectWithoutDefaultConstructorCloneTest() throws ReflectiveOperationException {
        ObjectUtils objectUtils = new ObjectUtils();

        ObjectWithoutDefaultConstructor input = new ObjectWithoutDefaultConstructor(42);

        ObjectWithoutDefaultConstructor output = (ObjectWithoutDefaultConstructor) objectUtils.copy(input);

        input.setAnInt(43);

        assert output.getAnInt() == 42;
    }

    @Test
    public void objectWithStaticCloneTest() throws ReflectiveOperationException {
        ObjectUtils objectUtils = new ObjectUtils();

        ObjectWithStatic input = new ObjectWithStatic();

        ObjectWithStatic output = (ObjectWithStatic) objectUtils.copy(input);

        input.setInteger(43);

        assert output.getInteger() == 42;
    }

    @Test
    public void objectWithArrayCloneTest() throws ReflectiveOperationException {
        ObjectUtils objectUtils = new ObjectUtils();

        ObjectWithArray input = new ObjectWithArray(new String[] {"1", "2"});

        ObjectWithArray output = (ObjectWithArray) objectUtils.copy(input);

        input.setStrings(new String[] {"3", "4"});

        assert output.getStrings()[0].equals("1");
        assert output.getStrings()[1].equals("2");
    }

    @Test
    public void objectWithFinalCloneTest() throws ReflectiveOperationException {
        ObjectUtils objectUtils = new ObjectUtils();

        ObjectWithFinal input = new ObjectWithFinal(new ClassD());

        ObjectWithFinal output = (ObjectWithFinal) objectUtils.copy(input);

        assert !getHexCode(output.getFinalD()).equals(getHexCode(input.getFinalD()));
    }
}