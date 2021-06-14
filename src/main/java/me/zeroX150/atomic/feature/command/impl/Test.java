package me.zeroX150.atomic.feature.command.impl;

import me.zeroX150.atomic.feature.command.Command;
import me.zeroX150.atomic.feature.gui.notifications.Notification;

public class Test extends Command {
    public Test() {
        super("Test", "amogus sus", "among", "sus", "test");
    }

    @Override
    public void onExecute(String[] args) {
        for (int i = 20; i > 0; i--) Notification.create(1000L * i, "monk", "ye");
    }
}
