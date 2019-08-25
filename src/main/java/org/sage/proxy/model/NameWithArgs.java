package org.sage.proxy.model;

import com.fasterxml.jackson.databind.JsonNode;

public class NameWithArgs {
    private String name;
    private JsonNode args;

    public NameWithArgs() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonNode getArgs() {
        return args;
    }

    public void setArgs(JsonNode args) {
        this.args = args;
    }
}
