package chypakk.model.resources.generator;

import chypakk.model.Castle;
import chypakk.model.resources.Resource;
import chypakk.model.resources.Wood;

public class Forest extends ResourceGenerator {

    public Forest(int interval, int amountPerInterval, int totalAmount, Castle castle) {
        super(interval, amountPerInterval, totalAmount, castle);
    }

    @Override
    protected Resource createResource() {
        return new Wood(amountPerInterval);
    }
}