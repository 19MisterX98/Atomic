package me.zeroX150.atomic.feature.module.impl.combat;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.MultiValue;
import me.zeroX150.atomic.helper.event.Event;
import me.zeroX150.atomic.helper.event.EventSystem;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class Criticals extends Module {
    MultiValue mode = this.config.create("Mode", "packet", "packet", "tphop");

    public Criticals() {
        super("Criticals", "more damage", ModuleType.COMBAT);
        EventSystem.registerEventHandler(Event.PACKET_SEND, event -> {
            if (Atomic.client.player == null || Atomic.client.getNetworkHandler() == null) return;
            if (event.getPacket() instanceof PlayerInteractEntityC2SPacket && this.isEnabled()) {
                Vec3d ppos = Atomic.client.player.getPos();
                switch (mode.getValue().toLowerCase()) {
                    case "packet" -> {
                        PlayerMoveC2SPacket.PositionAndOnGround p1 = new PlayerMoveC2SPacket.PositionAndOnGround(ppos.x, ppos.y + 0.2, ppos.z, true);
                        PlayerMoveC2SPacket.PositionAndOnGround p2 = new PlayerMoveC2SPacket.PositionAndOnGround(ppos.x, ppos.y, ppos.z, false);
                        PlayerMoveC2SPacket.PositionAndOnGround p3 = new PlayerMoveC2SPacket.PositionAndOnGround(ppos.x, ppos.y + 0.000011, ppos.z, false);
                        PlayerMoveC2SPacket.PositionAndOnGround p4 = new PlayerMoveC2SPacket.PositionAndOnGround(ppos.x, ppos.y, ppos.z, false);
                        Atomic.client.getNetworkHandler().sendPacket(p1);
                        Atomic.client.getNetworkHandler().sendPacket(p2);
                        Atomic.client.getNetworkHandler().sendPacket(p3);
                        Atomic.client.getNetworkHandler().sendPacket(p4);
                    }
                    case "tphop" -> {
                        PlayerMoveC2SPacket.PositionAndOnGround p5 = new PlayerMoveC2SPacket.PositionAndOnGround(ppos.x, ppos.y + 0.02, ppos.z, false);
                        PlayerMoveC2SPacket.PositionAndOnGround p6 = new PlayerMoveC2SPacket.PositionAndOnGround(ppos.x, ppos.y + 0.01, ppos.z, false);
                        Atomic.client.getNetworkHandler().sendPacket(p5);
                        Atomic.client.getNetworkHandler().sendPacket(p6);
                    }
                }
            }
        });

    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

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

