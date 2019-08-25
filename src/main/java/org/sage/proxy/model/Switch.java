package org.sage.proxy.model;

public class Switch {
    private boolean isOn;
    private String name;

    public Switch() {
    }

    public boolean isOn() {
        return isOn;
    }

    public void setIsOn(boolean on) {
        isOn = on;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Switch{" +
                "isOn=" + isOn +
                ", name='" + name + '\'' +
                '}';
    }
}
