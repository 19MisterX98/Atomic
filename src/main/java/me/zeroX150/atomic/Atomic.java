package me.zeroX150.atomic;

import me.zeroX150.atomic.feature.gui.clickgui.ClickGUI;
import me.zeroX150.atomic.feature.gui.screen.AltManager;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.helper.ConfigManager;
import me.zeroX150.atomic.helper.font.FontRenderer;
import me.zeroX150.atomic.helper.keybind.KeybindManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Atomic implements ModInitializer {

    public static final String MOD_ID = "atomic";
    public static final String MOD_NAME = "Atomic client";
    public static FontRenderer fontRenderer;
    public static FontRenderer monoFontRenderer;
    public static MinecraftClient client = MinecraftClient.getInstance();
    public static Logger LOGGER = LogManager.getLogger();

    public static Thread FAST_TICKER;

    public static void log(Level level, String message) {
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }

    @Override
    public void onInitialize() {
        log(Level.INFO, "Initializing");
        fontRenderer = new FontRenderer(Atomic.class.getClassLoader().getResourceAsStream("Font.ttf"), "default");
        monoFontRenderer = new FontRenderer(Atomic.class.getClassLoader().getResourceAsStream("Mono.ttf"), "mono");
        KeybindManager.init();
        ConfigManager.loadState();
        Runtime.getRuntime().addShutdownHook(new Thread(ConfigManager::saveState));
        FAST_TICKER = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (client.currentScreen instanceof AltManager altManager) {
                    altManager.fastTick();
                }
                if (Atomic.client.player == null || Atomic.client.world == null) continue;
                for (Module module : ModuleRegistry.getModules()) {
                    try {
                        if (module.isEnabled()) module.onFastTick();
                    } catch (Exception ignored) {
                    }
                }
                if (ClickGUI.INSTANCE != null) ClickGUI.INSTANCE.onFastTick();
            }
        }, "100_tps_ticker");
        FAST_TICKER.start();

        //TODO: Initializer
    }


}