package net.tkg.ModernMayhem.server.config;

import net.minecraftforge.fml.config.ModConfig;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.util.*;

public class ServerConfig {

    public static void init() {
        for(ArmorProperties armorConfig : ArmorProperties.values()) {
            ModernMayhemMod.getModLoadingContext().registerConfig(ModConfig.Type.SERVER, armorConfig.getConfig(), "modern-mayhem/armor-config/" + armorConfig.getName() + ".toml");
        }

        for(CuriosBodyProperties curiosConfig : CuriosBodyProperties.values()) {
            ModernMayhemMod.getModLoadingContext().registerConfig(ModConfig.Type.SERVER, curiosConfig.getConfig(), "modern-mayhem/curios-config/body/" + curiosConfig.getName() + ".toml");
        }

        for(CuriosFacewearProperties curiosConfig : CuriosFacewearProperties.values()) {
            ModernMayhemMod.getModLoadingContext().registerConfig(ModConfig.Type.SERVER, curiosConfig.getConfig(), "modern-mayhem/curios-config/facewear/" + curiosConfig.getName() + ".toml");
        }

        for(BackpackStorageProperties curiosConfig : BackpackStorageProperties.values()) {
            ModernMayhemMod.getModLoadingContext().registerConfig(ModConfig.Type.SERVER, curiosConfig.getConfig(), "modern-mayhem/curios-config/backpack/" + curiosConfig.getName() + ".toml");
        }

        for(RigStorageProperties curiosConfig : RigStorageProperties.values()) {
            ModernMayhemMod.getModLoadingContext().registerConfig(ModConfig.Type.SERVER, curiosConfig.getConfig(), "modern-mayhem/curios-config/body/storage/" + curiosConfig.getName() + ".toml");
        }

    }
}