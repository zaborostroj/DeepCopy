package objects;

public class ObjectWithInteger {

    public static final int staticFinalInt = 42;
    public static long staticLong = 42L;

    private Integer integer = 43;

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }
}
