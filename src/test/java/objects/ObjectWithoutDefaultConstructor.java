package objects;

public class ObjectWithoutDefaultConstructor {
    private int anInt;

    public ObjectWithoutDefaultConstructor(int anInt) {
        this.anInt = anInt;
    }

    public int getAnInt() {
        return anInt;
    }

    public void setAnInt(int anInt) {
        this.anInt = anInt;
    }
}
