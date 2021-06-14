package me.zeroX150.atomic.feature.module;

public enum ModuleType {
    TEST("Test"), TEST1("Test"), TES2T("Test"), TES3T("Test"), TE4ST("Test"), MOVEMENT("Movement"), RENDER("Render"), MISC("Miscellaneous"), COMBAT("Combat");


    String name;

    ModuleType(String n) {
        this.name = n;
    }

    public String getName() {
        return name;
    }
}
