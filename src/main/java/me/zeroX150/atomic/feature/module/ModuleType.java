package me.zeroX150.atomic.feature.module;

public enum ModuleType {
    TEST("Test"), MOVEMENT("Movement"), RENDER("Render"), MISC("Miscellaneous");


    String name;

    ModuleType(String n) {
        this.name = n;
    }

    public String getName() {
        return name;
    }
}
