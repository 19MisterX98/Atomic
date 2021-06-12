package me.zeroX150.atomic.feature.module.impl.movement;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.MultiValue;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import me.zeroX150.atomic.helper.event.Event;
import me.zeroX150.atomic.helper.event.EventSystem;
import me.zeroX150.atomic.mixin.network.PlayerMoveC2SPacketAccessor;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall extends Module {
    public MultiValue mode = this.config.create("Mode", "OnGround", "OnGround", "Packet", "BreakFall");
    SliderValue fallDist = this.config.create("Fall distance", 3, 0, 10, 1);

    public NoFall() {
        super("NoFall", "no fall damage", ModuleType.MOVEMENT);
        this.fallDist.showOnlyIf(() -> !this.mode.getValue().equalsIgnoreCase("onground"));
        EventSystem.registerEventHandler(Event.PACKET_SEND, event -> {
            if (event.getPacket() instanceof PlayerMoveC2SPacket) {
                if (mode.getValue().equalsIgnoreCase("onground")) {
                    ((PlayerMoveC2SPacketAccessor) event.getPacket()).setOnGround(true);
                }
            }
        });
    }

    @Override
    public void tick() {
        if (Atomic.client.player.fallDistance > fallDist.getValue()) {
            switch (mode.getValue().toLowerCase()) {
                case "packet" -> Atomic.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
                case "breakfall" -> {
                    Atomic.client.player.setVelocity(0, 0.1, 0);
                    Atomic.client.player.fallDistance = 0;
                }
            }
        }
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        return this.mode.getValue();
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}

