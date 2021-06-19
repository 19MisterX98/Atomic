package me.zeroX150.atomic;

import me.zeroX150.atomic.helper.ConfigManager;
import me.zeroX150.atomic.helper.keybind.KeybindManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Atomic implements ModInitializer {

    public static final String MOD_ID = "atomic";
    public static final String MOD_NAME = "Atomic client";
    public static MinecraftClient client = MinecraftClient.getInstance();
    public static Logger LOGGER = LogManager.getLogger();

    public static void log(Level level, String message) {
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }

    @Override
    public void onInitialize() {
        log(Level.INFO, "Initializing");
        KeybindManager.init();
        ConfigManager.loadState();
        //Runtime.getRuntime().addShutdownHook(new Thread(ConfigManager::saveState));
        //TODO: Initializer
    }

}