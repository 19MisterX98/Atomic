package me.zeroX150.atomic.feature.module.impl.movement;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.MultiValue;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import me.zeroX150.atomic.helper.event.Events;
import me.zeroX150.atomic.helper.event.PacketEvents;
import me.zeroX150.atomic.mixin.network.IPlayerMoveC2SPacketAccessor;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

/**
 * @see IPlayerMoveC2SPacketAccessor
 */
public class NoFall extends Module {
    public static MultiValue mode;
    SliderValue fallDist = this.config.create("Fall distance", 3, 0, 10, 1);

    public NoFall() {
        super("NoFall", "no fall damage", ModuleType.MOVEMENT);
        mode = this.config.create("Mode", "OnGround", "OnGround", "Packet", "BreakFall");
        this.fallDist.showOnlyIf(() -> !mode.getValue().equalsIgnoreCase("onground"));
        Events.Packets.registerEventHandler(PacketEvents.PACKET_SEND, event -> {
            if (!this.isEnabled()) return;
            if (event.getPacket() instanceof PlayerMoveC2SPacket) {
                if (mode.getValue().equalsIgnoreCase("onground")) {
                    ((IPlayerMoveC2SPacketAccessor) event.getPacket()).setOnGround(true);
                }
            }
        });
    }

    @Override
    public void tick() {
        if (Atomic.client.player == null || Atomic.client.getNetworkHandler() == null) return;
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
        return mode.getValue();
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}

