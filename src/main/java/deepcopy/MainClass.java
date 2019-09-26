package deepcopy;

import deepcopy.objects.Dummy;
import deepcopy.objects.DummyHolder;
import deepcopy.utils.ObjectUtils;

public class MainClass {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        ObjectUtils<DummyHolder> objectUtils = new ObjectUtils<>();

        Dummy inner = new Dummy();
        DummyHolder dummyHolder = new DummyHolder();

        dummyHolder.setDummy(inner);
        inner.setPrivateInteger(43);
        inner.setPrivateLong(43L);
        dummyHolder.setPrivateDouble(43.0);

        DummyHolder copy = objectUtils.copy(dummyHolder);

        inner.setPrivateInteger(44);
        inner.setPrivateLong(44L);
        dummyHolder.setPrivateDouble(44.0);

        System.out.println("original");
        System.out.println(dummyHolder.toString());

        System.out.println("copy");
        System.out.println(copy.toString());
    }
}
