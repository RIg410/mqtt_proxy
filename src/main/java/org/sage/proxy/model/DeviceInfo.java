package org.sage.proxy.model;

public class DeviceInfo {
    private String name;

    public DeviceInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "name='" + name + '\'' +
                '}';
    }
}
