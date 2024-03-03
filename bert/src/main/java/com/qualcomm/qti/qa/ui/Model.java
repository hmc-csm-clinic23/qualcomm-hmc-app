package com.qualcomm.qti.qa.ui;

public class Model {
    private String name;
    private String info;

    public Model(String name, String info) {
        this.name = name;
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }
}
