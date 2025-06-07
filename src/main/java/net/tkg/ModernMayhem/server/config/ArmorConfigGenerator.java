package net.tkg.ModernMayhem.server.config;

import net.minecraftforge.fml.config.ModConfig;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.util.ArmorProperties;

public class ArmorConfigGenerator {

    public static void init() {
        for (ArmorProperties armorConfig : ArmorProperties.values()) {
            // Using server config type because they are synced to the client and are used for server side only.
            ModernMayhemMod.getModLoadingContext().registerConfig(ModConfig.Type.SERVER, armorConfig.getConfig(), "modern-mayhem/armor-config/" + armorConfig.getName() + ".toml");
        }
    }
}
