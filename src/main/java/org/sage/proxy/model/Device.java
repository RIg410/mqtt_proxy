package org.sage.proxy.model;

public class Device {
    private boolean isOn;
    private String name;
    private int dim;

    public Device() {
    }

    public Device(boolean isOn, String name, int dim) {
        this.isOn = isOn;
        this.name = name;
        this.dim = dim;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDim() {
        return dim;
    }

    public void setDim(int dim) {
        this.dim = dim;
    }

    @Override
    public String toString() {
        return "Device{" +
                "isOn=" + isOn +
                ", name='" + name + '\'' +
                ", dim=" + dim +
                '}';
    }
}
