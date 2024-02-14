package com.telemedicine.myclinic.eenum;

public enum OrderStatus {

    UNKNOWN("UNKNOWN", 0), ORDERED("Ordered", 1),
    CANCELLED("Cancelled", 2), PAID("Paid", 3), DISPENSED("Dispensed", 4),
    PREPARED("Prepared", 5),;

    private final String key;
    private final Integer value;

    OrderStatus(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Integer getValue() {
        return value;
    }
}
