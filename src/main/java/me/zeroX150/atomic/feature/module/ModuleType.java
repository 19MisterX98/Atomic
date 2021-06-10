package me.zeroX150.atomic.feature.module;

public enum ModuleType {
    TEST("Test"), MOVEMENT("Movement"), RENDER("Render");


    String name;

    ModuleType(String n) {
        this.name = n;
    }

    public String getName() {
        return name;
    }
}
