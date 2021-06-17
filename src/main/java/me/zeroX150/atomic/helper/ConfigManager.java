package me.zeroX150.atomic.helper;

import com.google.common.base.Charsets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.feature.module.config.DynamicValue;
import me.zeroX150.atomic.helper.keybind.KeybindManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    public static boolean loaded = false;

    static File CONFIG_FILE;

    static {
        CONFIG_FILE = new File(Atomic.client.runDirectory.getAbsolutePath() + "/config.atomic");
    }

    public static void saveState() {
        if (!loaded) {
            System.out.println("Not saving config because we didnt load it yet");
            return;
        }
        System.out.println("Saving config");
        JsonObject base = new JsonObject();
        JsonArray enabled = new JsonArray();
        JsonArray config = new JsonArray();
        for (Module module : ModuleRegistry.getModules()) {
            if (module.isEnabled()) enabled.add(module.getName());
            JsonObject currentConfig = new JsonObject();
            currentConfig.addProperty("name", module.getName());
            JsonArray pairs = new JsonArray();
            for (DynamicValue<?> dynamicValue : module.config.getAll()) {
                JsonObject jesus = new JsonObject();
                jesus.addProperty("key", dynamicValue.getKey());
                jesus.addProperty("value", dynamicValue.getValue() + "");
                pairs.add(jesus);
            }
            currentConfig.add("pairs", pairs);
            config.add(currentConfig);
        }
        base.add("enabled", enabled);
        base.add("config", config);
        System.out.println(base);
        try {
            FileUtils.writeStringToFile(CONFIG_FILE, base.toString(), Charsets.UTF_8, false);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save config!");
        }
    }

    public static void loadState() {
        if (loaded) return;
        loaded = true;
        try {
            if (!CONFIG_FILE.isFile()) CONFIG_FILE.delete();
            if (!CONFIG_FILE.exists()) return;
            String retrv = FileUtils.readFileToString(CONFIG_FILE, Charsets.UTF_8);
            JsonObject config = new JsonParser().parse(retrv).getAsJsonObject();
            if (config.has("config") && config.get("config").isJsonArray()) {
                JsonArray configArray = config.get("config").getAsJsonArray();
                for (JsonElement jsonElement : configArray) {
                    if (jsonElement.isJsonObject()) {
                        JsonObject jobj = jsonElement.getAsJsonObject();
                        String name = jobj.get("name").getAsString();
                        Module j = ModuleRegistry.getByName(name);
                        if (j == null) continue;
                        if (jobj.has("pairs") && jobj.get("pairs").isJsonArray()) {
                            JsonArray pairs = jobj.get("pairs").getAsJsonArray();
                            for (JsonElement pair : pairs) {
                                JsonObject jo = pair.getAsJsonObject();
                                String key = jo.get("key").getAsString();
                                String value = jo.get("value").getAsString();
                                DynamicValue<?> val = j.config.get(key);
                                if (val != null) {
                                    Object newValue = TypeConverter.convert(value, val.getType());
                                    System.out.println(val.getKey() + " = " + value + " (" + newValue + " converted)");
                                    if (newValue != null) val.setValue(newValue);
                                }
                            }
                        }
                    }
                }
            }

            if (config.has("enabled") && config.get("enabled").isJsonArray()) {
                for (JsonElement enabled : config.get("enabled").getAsJsonArray()) {
                    String name = enabled.getAsString();
                    Module m = ModuleRegistry.getByName(name);
                    if (m != null) m.setEnabled(true);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load config!");
        } finally {
            KeybindManager.reload();
        }
    }
}
