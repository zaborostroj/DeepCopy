package objects;

public class ObjectWithFinal {

    private final ClassD finalD;

    public ObjectWithFinal(ClassD finalD) {
        this.finalD = finalD;
    }

    public ClassD getFinalD() {
        return finalD;
    }
}
