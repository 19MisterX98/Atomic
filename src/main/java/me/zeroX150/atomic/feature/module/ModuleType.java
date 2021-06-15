package me.zeroX150.atomic.feature.module;

public enum ModuleType {
    MOVEMENT("Movement"), RENDER("Render"), MISC("Miscellaneous"), COMBAT("Combat");


    String name;

    ModuleType(String n) {
        this.name = n;
    }

    public String getName() {
        return name;
    }
}
