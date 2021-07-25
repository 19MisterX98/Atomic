package me.zeroX150.atomic.feature.module.impl.misc;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.gui.notifications.Notification;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.List;

public class ChatSequence extends Module {
    public static List<String> messages = new ArrayList<>();
    public static long delay = 30;
    public static volatile boolean shouldRun = false;
    Thread runner=null;

    public ChatSequence() {
        super("ChatSequence","Follows a script and sends every message in chat", ModuleType.MISC);
    }

    @Override
    public void tick() {
    }

    @Override
    public void enable() {
        if (!shouldRun) {
            this.setEnabled(false);
            Notification.create(6000,"ChatSequence warning","Please only use this module via the .chatSequence command!");
            return;
        }
        if (messages.isEmpty()) {
            this.setEnabled(false);
            Notification.create(6000,"CharSequence error","You gotta configure some messages for me to send dude. .chatSequence command");
            return;
        }
        runner = new Thread(()->{
            for (String s : messages) {
                if (!shouldRun) break;
                if (Atomic.client.player == null || Atomic.client.world == null || Atomic.client.getNetworkHandler() == null) break;
                Atomic.client.player.sendChatMessage(s);
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Notification.create(5000,"ChatSequence","Done sending messages.");
            this.setEnabled(false);
            messages.clear();
        });
        runner.start();
    }

    @Override
    public void disable() {
        shouldRun = false;
        Notification.create(5000,"ChatSequence","Waiting for sender thread cleanup...");
    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}

