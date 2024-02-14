package com.telemedicine.myclinic.eenum;

public enum Success {

    SUCCESSCODE(100), VERIFICTIONMATCHED(210);

    private final int value;

    private Success(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
