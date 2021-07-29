package me.zeroX150.atomic.feature.module;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.impl.combat.AutoEndermanAngry;
import me.zeroX150.atomic.feature.module.impl.combat.AutoLog;
import me.zeroX150.atomic.feature.module.impl.combat.Criticals;
import me.zeroX150.atomic.feature.module.impl.combat.Killaura;
import me.zeroX150.atomic.feature.module.impl.exploit.*;
import me.zeroX150.atomic.feature.module.impl.external.*;
import me.zeroX150.atomic.feature.module.impl.misc.AntiParticleCrash;
import me.zeroX150.atomic.feature.module.impl.misc.AutoFish;
import me.zeroX150.atomic.feature.module.impl.misc.ChatSequence;
import me.zeroX150.atomic.feature.module.impl.misc.InventoryCleaner;
import me.zeroX150.atomic.feature.module.impl.movement.*;
import me.zeroX150.atomic.feature.module.impl.render.*;
import me.zeroX150.atomic.feature.module.impl.render.OreSim.OreSim;
import me.zeroX150.atomic.feature.module.impl.testing.TestModule;
import me.zeroX150.atomic.feature.module.impl.world.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleRegistry {
    static List<Module> modules = new ArrayList<>();

    static {
        /* mods to add
         *
         * elytrafly
         * autototem
         * antilevitate
         * hitboxes
         * shulkerpeek
         * */

        modules.add(new TestModule());
        modules.add(new ClickGUI());
        modules.add(new AirJump());
        modules.add(new ArrowJuke());
        modules.add(new AutoSneak());
        modules.add(new Blink());
        modules.add(new EntityFly());
        modules.add(new Hud());
        modules.add(new Boost());
        modules.add(new Flight());
        modules.add(new Jesus());
        modules.add(new MoonGravity());
        modules.add(new NoFall());
        modules.add(new NoJumpCooldown());
        modules.add(new ClickFly());
        modules.add(new Speed());
        modules.add(new Sprint());
        modules.add(new Step());
        modules.add(new AutoLog());
        modules.add(new OreSim());
        modules.add(new Nuker());
        modules.add(new Criticals());
        modules.add(new Killaura());
        modules.add(new AntiAntiXray());
        modules.add(new XRAY());
        modules.add(new AntiOffhandCrash());
        modules.add(new BoatPhase());
        modules.add(new SoundLogger());
        modules.add(new PingSpoof());
        modules.add(new AntiPacketKick());
        modules.add(new Fullbright());
        modules.add(new NameTags());
        modules.add(new ClientConfig());
        modules.add(new Tracers());
        modules.add(new ESP());
        modules.add(new Alts());
        modules.add(new HologramAura());
        modules.add(new TexPackSpoof());
        modules.add(new Bunker());
        modules.add(new SlotSpammer());
        modules.add(new VerticalPhase());
        modules.add(new Freecam());
        modules.add(new NoPush());
        modules.add(new BuildLimit());
        modules.add(new WaterClutch());
        modules.add(new Zoom());
        modules.add(new AutoEndermanAngry());
        modules.add(new MidAirPlace());
        modules.add(new Dupe());
        modules.add(new InventoryWalk());
        modules.add(new TargetHud());
        modules.add(new FarmingAura());
        modules.add(new BetterCrosshair());
        modules.add(new NoBreakDelay());
        modules.add(new AutoFish());
        modules.add(new ChestESP());
        modules.add(new InventoryCleaner());
        modules.add(new OffhandCrash());
        modules.add(new BlockSpammer());
        modules.add(new NoRender());
        modules.add(new VanillaSpoof());
        modules.add(new Scaffold());
        modules.add(new AntiVoid());
        modules.add(new Phase());
        modules.add(new AntiParticleCrash());
        modules.add(new NameProtect());
        modules.add(new LeverAura());
        modules.add(new ChatSequence());
        modules.add(new GodBridge());
        modules.add(new AntiReducedDebugInfo());
        modules.add(new FastUse());

        modules = modules.stream().sorted(Comparator.comparingDouble(value -> -Atomic.fontRenderer.getStringWidth(value.getName()))).collect(Collectors.toList());
    }

    public static List<Module> getModules() {
        return modules;
    }

    public static Module getByClass(Class<? extends Module> clazz) {
        for (Module module : getModules()) {
            if (module.getClass() == clazz) return module;
        }
        return null;
    }

    public static Module getByName(String n) {
        for (Module module : getModules()) {
            if (module.getName().equalsIgnoreCase(n)) return module;
        }
        return null;
    }
}
