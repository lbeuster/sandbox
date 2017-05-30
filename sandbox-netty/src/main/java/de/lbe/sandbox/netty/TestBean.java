package de.lbe.sandbox.netty;

import java.io.Serializable;
import java.util.List;

public class TestBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private List<String> properties;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getProperties() {
        return properties;
    }

    public void setProperties(List<String> properties) {
        this.properties = properties;
    }
}