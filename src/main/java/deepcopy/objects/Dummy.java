package deepcopy.objects;

public class Dummy {
    private Integer privateInteger = 42;
    private Long privateLong = 42L;

    public Integer getPrivateInteger() {
        return privateInteger;
    }

    public void setPrivateInteger(Integer privateInteger) {
        this.privateInteger = privateInteger;
    }

    public Long getPrivateLong() {
        return privateLong;
    }

    public void setPrivateLong(Long privateLong) {
        this.privateLong = privateLong;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + ": " + this.privateInteger + " " + this.privateLong;
    }
}
