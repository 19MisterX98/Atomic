package me.zeroX150.atomic.feature.command.impl;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.command.Command;
import me.zeroX150.atomic.helper.Client;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;

public class Effect extends Command {
    public Effect() {
        super("Effect", "gives you an effect client side", "effect", "eff");
    }

    @Override
    public void onExecute(String[] args) {
        if (args.length == 0) {
            Client.notifyUser("action please");
            return;
        }
        switch (args[0].toLowerCase()) {
            case "give" -> {
                if (args.length < 4) {
                    Client.notifyUser("effect id, duration and strength pls");
                    return;
                }
                int id = Client.tryParseInt(args[1], -1);
                if (id == -1) {
                    Client.notifyUser("idk about that status effect ngl");
                    return;
                }
                int duration = Client.tryParseInt(args[2], 30);
                int strength = Client.tryParseInt(args[3], 1);
                StatusEffect effect = StatusEffect.byRawId(id);
                if (effect == null) {
                    Client.notifyUser("idk about that status effect ngl");
                    return;
                }
                StatusEffectInstance inst = new StatusEffectInstance(effect, duration, strength);
                Atomic.client.player.addStatusEffect(inst);
            }
            case "clear" -> {
                for (StatusEffectInstance statusEffect : Atomic.client.player.getStatusEffects().toArray(new StatusEffectInstance[0])) {
                    Atomic.client.player.removeStatusEffect(statusEffect.getEffectType());
                }
            }
            default -> Client.notifyUser("\"give\" and \"clear\" only pls");
        }
    }
}
