package objects;

public class ObjectWithStatic {

    public static final double staticFinalInt = 42.0;
    public static long staticLong = 42L;

    private Integer integer = 42;

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }
}
