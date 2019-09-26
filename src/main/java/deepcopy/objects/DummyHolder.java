package deepcopy.objects;

public class DummyHolder {
    private Dummy dummy;
    private Double privateDouble = 42.0;

    public Dummy getDummy() {
        return dummy;
    }

    public void setDummy(Dummy dummy) {
        this.dummy = dummy;
    }

    public Double getPrivateDouble() {
        return privateDouble;
    }

    public void setPrivateDouble(Double privateDouble) {
        this.privateDouble = privateDouble;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + ": " + this.privateDouble
                + "\n  └> " + this.dummy;
    }
}
